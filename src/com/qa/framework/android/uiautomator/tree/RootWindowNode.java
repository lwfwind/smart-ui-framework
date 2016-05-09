package com.qa.framework.android.uiautomator.tree;


public class RootWindowNode extends BasicTreeNode {

    private final String mWindowName;
    private Object[] mCachedAttributesArray;
    private int mRotation;

    public RootWindowNode(String windowName) {
        this(windowName, 0);
    }

    public RootWindowNode(String windowName, int rotation) {
        mWindowName = windowName;
        mRotation = rotation;
    }

    @Override
    public String toString() {
        return mWindowName;
    }

    @Override
    public Object[] getAttributesArray() {
        if (mCachedAttributesArray == null) {
            mCachedAttributesArray = new Object[]{new AttributePair("window-name", mWindowName)};
        }
        return mCachedAttributesArray;
    }

    public int getRotation() {
        return mRotation;
    }
}
