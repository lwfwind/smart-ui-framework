package com.qa.framework.library.android.uiautomator.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicTreeNode {

    private static final BasicTreeNode[] CHILDREN_TEMPLATE = new BasicTreeNode[]{};
    protected final List<BasicTreeNode> mChildren = new ArrayList<BasicTreeNode>();
    public int x, y, width, height;
    protected BasicTreeNode mParent;
    // whether the boundary fields are applicable for the node or not
    // RootWindowNode has no bounds, but UiNodes should
    protected boolean mHasBounds = false;

    public void addChild(BasicTreeNode child) {
        if (child == null) {
            throw new NullPointerException("Cannot add null child");
        }
        if (mChildren.contains(child)) {
            throw new IllegalArgumentException("node already a child");
        }
        mChildren.add(child);
        child.mParent = this;
    }

    public List<BasicTreeNode> getChildrenList() {
        return Collections.unmodifiableList(mChildren);
    }

    public BasicTreeNode[] getChildren() {
        return mChildren.toArray(CHILDREN_TEMPLATE);
    }

    public BasicTreeNode getParent() {
        return mParent;
    }

    public boolean hasChild() {
        return mChildren.size() != 0;
    }

    public int getChildCount() {
        return mChildren.size();
    }

    public void clearAllChildren() {
        for (BasicTreeNode child : mChildren) {
            child.clearAllChildren();
        }
        mChildren.clear();
    }

    /**
     * Find nodes in the tree containing the coordinate
     * <p/>
     * The found node should have bounds covering the coordinate, and none of its children's
     * bounds covers it. Depending on the layout, some app may have multiple nodes matching it,
     * the caller must provide a {@link IFindNodeListener} to receive all found nodes
     *
     * @param px
     * @param py
     * @return
     */
    public boolean findLeafMostNodesAtPoint(int px, int py, IFindNodeListener listener) {
        boolean foundInChild = false;
        for (BasicTreeNode node : mChildren) {
            foundInChild |= node.findLeafMostNodesAtPoint(px, py, listener);
        }
        // checked all children, if at least one child covers the point, return directly
        if (foundInChild) return true;
        // check self if the node has no children, or no child nodes covers the point
        if (mHasBounds) {
            if (x <= px && px <= x + width && y <= py && py <= y + height) {
                listener.onFoundNode(this);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Object[] getAttributesArray() {
        return null;
    }

    ;

    public static interface IFindNodeListener {
        void onFoundNode(BasicTreeNode node);
    }
}
