package facebook.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.mapreduce.Reducer;

import facebook.custom.writable.Friend;
import facebook.custom.writable.FriendArray;
import facebook.custom.writable.FriendPair;

public class FacebookFriendReducer extends Reducer<FriendPair, FriendArray, FriendPair, FriendArray> {
	@Override
	protected void reduce(FriendPair key, Iterable<FriendArray> values, Context context)
			throws IOException, InterruptedException {
		List<Friend[]> flist = new ArrayList<>();
		List<Friend> commonFriendList = new ArrayList<>();
		int count = 0;
		for (FriendArray farray : values) {
			Friend[] f = Arrays.copyOf(farray.get(), farray.get().length, Friend[].class);
			flist.add(f);
			count++;
		}

		if (count != 2) {
			return;
		}

		for (Friend of : flist.get(0)) {
			for (Friend inf : flist.get(1)) {
				if (of.equals(inf)) {
					commonFriendList.add(of);
				}
			}
		}

		Friend[] commonFriendArray = Arrays.copyOf(commonFriendList.toArray(), commonFriendList.toArray().length,
				Friend[].class);

		context.write(key, new FriendArray(Friend.class, commonFriendArray));

	}
}
