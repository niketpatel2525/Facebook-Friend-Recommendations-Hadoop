package com.facebook.friend.recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.facebook.friend.Friendship;

public class Map extends Mapper<LongWritable, Text, LongWritable, Friendship> {
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] line = value.toString().split("\t");
		Long fromUser = Long.parseLong(line[0]);
		List<Long> toUsers = new ArrayList<Long>();
		if (line.length == 2) {
			StringTokenizer token = new StringTokenizer(line[1], ",");
			while (token.hasMoreTokens()) {
				Long toUser = Long.parseLong(token.nextToken());
				toUsers.add(toUser);
				context.write(new LongWritable(fromUser), new Friendship(toUser, -1L));
			}

			for (int i = 0; i < toUsers.size(); i++) {
				for (int j = i + 1; j < toUsers.size(); j++) {
					context.write(new LongWritable(toUsers.get(i)), new Friendship(toUsers.get(j), fromUser));
					context.write(new LongWritable(toUsers.get(j)), new Friendship(toUsers.get(i), fromUser));
				}
			}
		}

	}
}
