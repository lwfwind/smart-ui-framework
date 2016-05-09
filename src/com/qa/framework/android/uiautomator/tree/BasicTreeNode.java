package com.qa.framework.android.uiautomator.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Basic tree node.
 */
public class BasicTreeNode {

    private static final BasicTreeNode[] CHILDREN_TEMPLATE = new BasicTreeNode[]{};
    /**
     * The M children.
     */
    protected final List<BasicTreeNode> mChildren = new ArrayList<BasicTreeNode>();
    /**
     * The X.
     */
    public int x, /**
     * The Y.
     */
    y, /**
     * The Width.
     */
    width, /**
     * The Height.
     */
    height;
    /**
     * The M parent.
     */
    protected BasicTreeNode mParent;
    /**
     * The M has bounds.
     */
// whether the boundary fields are applicable for the node or not
    // RootWindowNode has no bounds, but UiNodes should
    protected boolean mHasBounds = false;

    /**
     * Add child.
     *
     * @param child the child
     */
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

    /**
     * Gets children list.
     *
     * @return the children list
     */
    public List<BasicTreeNode> getChildrenList() {
        return Collections.unmodifiableList(mChildren);
    }

    /**
     * Get children basic tree node [ ].
     *
     * @return the basic tree node [ ]
     */
    public BasicTreeNode[] getChildren() {
        return mChildren.toArray(CHILDREN_TEMPLATE);
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public BasicTreeNode getParent() {
        return mParent;
    }

    /**
     * Has child boolean.
     *
     * @return the boolean
     */
    public boolean hasChild() {
        return mChildren.size() != 0;
    }

    /**
     * Gets child count.
     *
     * @return the child count
     */
    public int getChildCount() {
        return mChildren.size();
    }

    /**
     * Clear all children.
     */
    public void clearAllChildren() {
        for (BasicTreeNode child : mChildren) {
            child.clearAllChildren();
        }
        mChildren.clear();
    }

    /**
     * Find nodes in the tree containing the coordinate
     * The found node should have bounds covering the coordinate, and none of its children's
     * bounds covers it. Depending on the layout, some app may have multiple nodes matching it,
     * the caller must provide a {@link IFindNodeListener} to receive all found nodes
     *
     * @param px       the px
     * @param py       the py
     * @param listener the listener
     * @return the boolean
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

    /**
     * Get attributes array object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getAttributesArray() {
        return null;
    }

    ;

    /**
     * The interface Find node listener.
     */
    public static interface IFindNodeListener {
        /**
         * On found node.
         *
         * @param node the node
         */
        void onFoundNode(BasicTreeNode node);
    }
}
