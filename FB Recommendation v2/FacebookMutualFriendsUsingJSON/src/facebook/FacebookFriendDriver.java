package facebook;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;

import facebook.custom.writable.FriendArray;
import facebook.custom.writable.FriendPair;
import facebook.mapreduce.FacebookFriendMapper;
import facebook.mapreduce.FacebookFriendReducer;

public class FacebookFriendDriver extends Configured implements Tool {
	public static void main(String[] args) throws IOException {

	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: FacebookFriendDriver <input path> <output path>");
			System.exit(-1);
		}

		Job fb = new Job(getConf(), "Facebook Friend Recommendation");
		fb.setJarByClass(FacebookFriendDriver.class);
		// Set input and output locations

		FileInputFormat.addInputPath(fb, new Path(args[0]));
		FileOutputFormat.setOutputPath(fb, new Path(args[1]));

		// Set Input and Output formats
		fb.setInputFormatClass(TextInputFormat.class);
		fb.setOutputFormatClass(SequenceFileOutputFormat.class);
		// Set Mapper and Reduce classes
		fb.setMapperClass(FacebookFriendMapper.class);
		fb.setReducerClass(FacebookFriendReducer.class);

		// Output types
		fb.setMapOutputKeyClass(FriendPair.class);
		fb.setMapOutputValueClass(FriendArray.class);

		fb.setOutputKeyClass(FriendPair.class);
		fb.setOutputValueClass(FriendArray.class);

		// Submit job

		return fb.waitForCompletion(true) ? 0 : 1;
	}
}
