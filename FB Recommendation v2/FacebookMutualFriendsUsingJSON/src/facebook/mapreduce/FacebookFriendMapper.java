package facebook.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import facebook.custom.writable.Friend;
import facebook.custom.writable.FriendArray;
import facebook.custom.writable.FriendPair;

public class FacebookFriendMapper extends Mapper<LongWritable, Text, FriendPair, FriendArray> {

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, FriendPair, FriendArray>.Context context)
			throws IOException, InterruptedException {

		StringTokenizer st = new StringTokenizer(value.toString(), "\t");
		String person = st.nextToken();
		String friends = st.nextToken();

		Friend f1 = populateFriend(person);
		List<Friend> friendList = populateFriendList(friends);

		Friend[] friendArray = Arrays.copyOf(friendList.toArray(), friendList.toArray().length, Friend[].class);
		FriendArray farray = new FriendArray(Friend.class, friendArray);

		for (Friend f2 : friendList) {
			FriendPair fp = new FriendPair(f1, f2);
			context.write(fp, farray);
		}
	}

	private Friend populateFriend(String friendJSON) {
		JsonParser parser = new JsonParser();
		Friend friend = null;
		try {
			Object obj = (Object) parser.parse(friendJSON);
			JsonObject jsonObject = (JsonObject) obj;
			Long lid = jsonObject.get("id").getAsLong();
			IntWritable id = new IntWritable(lid.intValue());
			Text name = new Text(jsonObject.get("name").getAsString());
			Text homeTown = new Text(jsonObject.get("hometown").getAsString());
			friend = new Friend(id, name, homeTown);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friend;
	}

	private List<Friend> populateFriendList(String friendJSON) {
		List<Friend> friendList = new ArrayList<>();
		try {
			JsonParser parser = new JsonParser();
			Object ob = (Object) parser.parse(friendJSON.toString());
			JsonArray jsonArray = (JsonArray) ob;

			for (Object obj : jsonArray) {
				JsonObject jsonObject = (JsonObject) obj;
				Long lid = jsonObject.get("id").getAsLong();
				IntWritable id = new IntWritable(lid.intValue());
				Text name = new Text(jsonObject.get("name").getAsString());
				Text homeTown = new Text(jsonObject.get("hometown").getAsString());
				Friend friend = new Friend(id, name, homeTown);
				friendList.add(friend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendList;
	}

}
