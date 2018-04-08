package facebook.custom.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class FriendPair implements WritableComparable {

	Friend first;
	Friend second;

	public FriendPair() {
		first = new Friend();
		second = new Friend();
	}

	public FriendPair(Friend first, Friend second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
	}

	@Override
	public int compareTo(Object o) {
		FriendPair pair2 = (FriendPair) o;
		int cmp = -1;
		if (getFirst().compareTo(pair2.getFirst()) == 0 || getFirst().compareTo(pair2.getSecond()) == 0)
			cmp = 0;
		if (cmp != 0) {
			return cmp;
		}
		cmp = -1;
		if (getSecond().compareTo(pair2.getFirst()) == 0 || getSecond().compareTo(pair2.getSecond()) == 0) {
			cmp = 0;
		}
		return cmp;
	}

	@Override
	public boolean equals(Object o) {
		FriendPair pair2 = (FriendPair) o;
		boolean eq = getFirst().equals(pair2.getFirst()) || getFirst().equals(pair2.getSecond());
		if (!eq)
			return eq;

		return getSecond().equals(pair2.getFirst()) || getSecond().equals(pair2.getSecond());
	}

	public Friend getFirst() {
		return first;
	}

	public void setFirst(Friend first) {
		this.first = first;
	}

	public Friend getSecond() {
		return second;
	}

	public void setSecond(Friend second) {
		this.second = second;
	}

}
