package com.ui.automation.framework.android.uiautomator.tree;


/**
 * The type Root window node.
 */
public class RootWindowNode extends BasicTreeNode {

    private final String mWindowName;
    private Object[] mCachedAttributesArray;
    private int mRotation;

    /**
     * Instantiates a new Root window node.
     *
     * @param windowName the window name
     */
    public RootWindowNode(String windowName) {
        this(windowName, 0);
    }

    /**
     * Instantiates a new Root window node.
     *
     * @param windowName the window name
     * @param rotation   the rotation
     */
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

    /**
     * Gets rotation.
     *
     * @return the rotation
     */
    public int getRotation() {
        return mRotation;
    }
}
