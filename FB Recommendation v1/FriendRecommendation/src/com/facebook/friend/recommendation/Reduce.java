package com.facebook.friend.recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.facebook.friend.Friendship;

public class Reduce extends Reducer<LongWritable, Friendship, LongWritable, Text> {

	@Override
	public void reduce(LongWritable key, Iterable<Friendship> values, Context context)
			throws IOException, InterruptedException {

		final Map<Long, List> mutualFriends = new HashMap<>();

		for (Friendship f : values) {
			boolean isAlreadyFriend = (f.mutualFriend == -1L);
			Long toUser = f.user;
			Long mutualFriend = f.mutualFriend;

			if (mutualFriends.containsKey(toUser)) {
				if (isAlreadyFriend) {
					mutualFriends.put(toUser, null);
				} else if (mutualFriends.get(toUser) != null) {
					mutualFriends.get(toUser).add(mutualFriend);
				}
			} else {
				if (!isAlreadyFriend) {
					List<Long> l = new ArrayList<>();
					l.add(mutualFriend);
					mutualFriends.put(toUser, l);
				} else {
					mutualFriends.put(toUser, null);
				}
			}
		}

		SortedMap<Long, List> sortedMutualFriendList = new TreeMap<Long, List>(new Comparator<Long>() {

			@Override
			public int compare(Long k1, Long k2) {
				// TODO Auto-generated method stub
				Integer v1 = mutualFriends.get(k1).size();
				Integer v2 = mutualFriends.get(k2).size();
				if (v1 > v2) {
					return -1;
				} else if (v1.equals(v2) && k1 < k2) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		for (Map.Entry<Long, List> entry : mutualFriends.entrySet()) {
			if (entry.getValue() != null) {
				sortedMutualFriendList.put(entry.getKey(), entry.getValue());
			}
		}
		int i = 0;
		String output = "";
		for (Map.Entry<Long, List> entry : sortedMutualFriendList.entrySet()) {
			if (i == 0) {
				output = entry.getKey().toString() + " ( " + entry.getValue().size() + " : " + entry.getValue() + ")";
			} else {
				output += "," + entry.getKey().toString() + " ( " + entry.getValue().size() + " : " + entry.getValue()
						+ ")";
			}
			i++;
		}
		context.write(key, new Text(output));
	}
}
