package com.qa.framework.android.uiautomator;

import com.qa.framework.android.uiautomator.tree.AttributePair;
import com.qa.framework.android.uiautomator.tree.BasicTreeNode;
import com.qa.framework.android.uiautomator.tree.UiHierarchyXmlLoader;
import com.qa.framework.android.uiautomator.tree.UiNode;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The type Ui automator model.
 */
public class UiAutomatorModel {
    private BasicTreeNode mRootNode;
    private BasicTreeNode mSelectedNode;
    private Rectangle mCurrentDrawingRect;
    private List<Rectangle> mNafNodes;
    private boolean mExploreMode = true;
    private boolean mShowNafNodes = false;
    private List<BasicTreeNode> mNodelist;
    private Set<String> mSearchKeySet = new HashSet<String>();

    /**
     * Instantiates a new Ui automator model.
     *
     * @param xmlDumpFile the xml dump file
     */
    public UiAutomatorModel(File xmlDumpFile) {
        this.mSearchKeySet.add("text");
        this.mSearchKeySet.add("content-desc");

        UiHierarchyXmlLoader loader = new UiHierarchyXmlLoader();
        BasicTreeNode rootNode = loader.parseXml(xmlDumpFile.getAbsolutePath());
        if (rootNode == null) {
            System.err.println("null rootnode after parsing.");
            throw new IllegalArgumentException("Invalid ui automator hierarchy file.");
        }
        this.mNafNodes = loader.getNafNodes();
        if (this.mRootNode != null) {
            this.mRootNode.clearAllChildren();
        }
        this.mRootNode = rootNode;
        this.mExploreMode = true;
        this.mNodelist = loader.getAllNodes();
    }

    /**
     * Gets xml root node.
     *
     * @return the xml root node
     */
    public BasicTreeNode getXmlRootNode() {
        return this.mRootNode;
    }

    /**
     * Gets selected node.
     *
     * @return the selected node
     */
    public BasicTreeNode getSelectedNode() {
        return this.mSelectedNode;
    }

    /**
     * Sets selected node.
     *
     * @param node the node
     */
    public void setSelectedNode(BasicTreeNode node) {
        this.mSelectedNode = node;
        if ((this.mSelectedNode instanceof UiNode)) {
            UiNode uiNode = (UiNode) this.mSelectedNode;
            this.mCurrentDrawingRect = new Rectangle(uiNode.x, uiNode.y, uiNode.width, uiNode.height);
        } else {
            this.mCurrentDrawingRect = null;
        }
    }

    /**
     * Gets current drawing rect.
     *
     * @return the current drawing rect
     */
    public Rectangle getCurrentDrawingRect() {
        return this.mCurrentDrawingRect;
    }

    /**
     * Is explore mode boolean.
     *
     * @return the boolean
     */
    public boolean isExploreMode() {
        return this.mExploreMode;
    }

    /**
     * Sets explore mode.
     *
     * @param exploreMode the explore mode
     */
    public void setExploreMode(boolean exploreMode) {
        this.mExploreMode = exploreMode;
    }

    /**
     * Toggle explore mode.
     */
    public void toggleExploreMode() {
        this.mExploreMode = (!this.mExploreMode);
    }

    /**
     * Gets naf nodes.
     *
     * @return the naf nodes
     */
    public List<Rectangle> getNafNodes() {
        return this.mNafNodes;
    }

    /**
     * Toggle show naf.
     */
    public void toggleShowNaf() {
        this.mShowNafNodes = (!this.mShowNafNodes);
    }

    /**
     * Should show naf nodes boolean.
     *
     * @return the boolean
     */
    public boolean shouldShowNafNodes() {
        return this.mShowNafNodes;
    }

    /**
     * Search node list.
     *
     * @param tofind the tofind
     * @return the list
     */
    public List<BasicTreeNode> searchNode(String tofind) {
        List<BasicTreeNode> result = new LinkedList<BasicTreeNode>();
        for (BasicTreeNode node : this.mNodelist) {
            Object[] attrs = node.getAttributesArray();
            for (Object attr : attrs) {
                if (this.mSearchKeySet.contains(((AttributePair) attr).key)) {
                    if (((AttributePair) attr).value.toLowerCase().contains(tofind.toLowerCase())) {
                        result.add(node);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static class MinAreaFindNodeListener
            implements BasicTreeNode.IFindNodeListener {
        /**
         * The M node.
         */
        BasicTreeNode mNode = null;

        public void onFoundNode(BasicTreeNode node) {
            if (this.mNode == null) {
                this.mNode = node;
            } else if (node.height * node.width < this.mNode.height * this.mNode.width) {
                this.mNode = node;
            }
        }
    }
}
