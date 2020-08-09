package com.mishiranu.dashchan.ui.navigator.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import com.mishiranu.dashchan.ui.navigator.PageFragment;

public class SavedPageItem implements Parcelable {
	public final StackItem stackItem;
	public final long createdRealtime;
	public final String threadTitle;
	public final boolean returnable;

	public SavedPageItem(StackItem stackItem, long createdRealtime, String threadTitle, boolean returnable) {
		this.stackItem = stackItem;
		this.createdRealtime = createdRealtime;
		this.threadTitle = threadTitle;
		this.returnable = returnable;
	}

	private Pair<PageFragment, PageItem> createInternal(Page page) {
		PageFragment fragment = (PageFragment) stackItem.create(page == null ? null
				: newFragment -> new PageFragment(page, ((PageFragment) newFragment).getRetainId()));
		PageItem pageItem = new PageItem();
		pageItem.createdRealtime = createdRealtime;
		pageItem.threadTitle = threadTitle;
		pageItem.returnable = returnable;
		return new Pair<>(fragment, pageItem);
	}

	public Pair<PageFragment, PageItem> create() {
		return createInternal(null);
	}

	public Pair<PageFragment, PageItem> createWithNewPage(Page page) {
		if (page == null) {
			throw new IllegalArgumentException();
		}
		return createInternal(page);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (stackItem != null ? 1 : 0));
		if (stackItem != null) {
			stackItem.writeToParcel(dest, flags);
		}
		dest.writeLong(createdRealtime);
		dest.writeString(threadTitle);
		dest.writeByte((byte) (returnable ? 1 : 0));
	}

	public static final Creator<SavedPageItem> CREATOR = new Creator<SavedPageItem>() {
		@Override
		public SavedPageItem createFromParcel(Parcel in) {
			StackItem stackItem = in.readByte() != 0 ? StackItem.CREATOR.createFromParcel(in) : null;
			long createdRealtime = in.readLong();
			String threadTitle = in.readString();
			boolean returnable = in.readByte() != 0;
			return new SavedPageItem(stackItem, createdRealtime, threadTitle, returnable);
		}

		@Override
		public SavedPageItem[] newArray(int size) {
			return new SavedPageItem[size];
		}
	};
}
