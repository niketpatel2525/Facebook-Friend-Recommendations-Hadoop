package com.facebook.friend;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Friendship implements Writable {
	public Long user;
	public Long mutualFriend;

	public Friendship(Long user, Long mutualFriend) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.mutualFriend = mutualFriend;
	}

	public Friendship() {
		// TODO Auto-generated constructor stub
		this(-1L, -1L);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		user = in.readLong();
		mutualFriend = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeLong(user);
		out.writeLong(mutualFriend);
	}

	@Override
	public String toString() {
		return "Friendship [user=" + Long.toString(user) + ", mutualFriend=" + Long.toString(mutualFriend) + "]";
	}

}
