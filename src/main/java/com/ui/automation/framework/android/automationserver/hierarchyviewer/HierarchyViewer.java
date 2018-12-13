package com.ui.automation.framework.android.automationserver.hierarchyviewer;

import com.android.ddmlib.IDevice;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.device.DeviceBridge;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.device.ViewNode;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.device.Window;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

/**
 * The type Hierarchy viewer.
 */
public class HierarchyViewer {
    private static final String TAG = "hierarchyviewer";
    private static Logger logger = Logger.getLogger(HierarchyViewer.class);
    private IDevice device = null;


    /**
     * Instantiates a new Hierarchy viewer.
     *
     * @param device the device
     */
    public HierarchyViewer(IDevice device) {
        this.device = device;
    }

    /**
     * Gets element center by text.
     *
     * @param text  the text
     * @param index the index
     * @return the element center by text
     */
    public Point getElementCenterByText(String text, int index) {
        Rectangle rectangle = getElementLocationByText(text, index);
        if (rectangle != null) {
            return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        }
        return null;
    }

    /**
     * Gets element text by id.
     *
     * @param id the id
     * @return the element text by id
     */
    public String getElementTextById(String id) {
        com.ui.automation.framework.android.automationserver.hierarchyviewer.device.Window[] windows = DeviceBridge.loadWindows(device);
        for (com.ui.automation.framework.android.automationserver.hierarchyviewer.device.Window window : windows) {
            ViewNode viewNode = DeviceBridge.loadWindowData(window);
            if (viewNode != null) {
                ArrayList<ViewNode> resultNodes = new ArrayList<>();
                searchElementRecursionById(viewNode, id, resultNodes);
                if (resultNodes.size() > 0) {
                    ViewNode node = resultNodes.get(0);
                    return node.namedProperties.get("text:mText").value;
                }
            }
        }
        return null;
    }


    /**
     * Gets element location by text.
     *
     * @param text  the text
     * @param index the index
     * @return the element location by text
     */
    public Rectangle getElementLocationByText(String text, int index) {
        com.ui.automation.framework.android.automationserver.hierarchyviewer.device.Window[] windows = DeviceBridge.loadWindows(device);
        for (Window window : windows) {
            ViewNode viewNode = DeviceBridge.loadWindowData(window);
            if (viewNode != null) {
                ArrayList<ViewNode> resultNodes = new ArrayList<>();
                searchElementRecursionByText(viewNode, text, resultNodes, index);
                if (resultNodes.size() > index) {
                    ViewNode node = resultNodes.get(index);
                    ViewNode.Property mLeftProperty = node.namedProperties.get("layout:mLeft");
                    ViewNode.Property mTopProperty = node.namedProperties.get("layout:mTop");
                    Point point = new Point(Integer.parseInt(mLeftProperty.value), Integer.parseInt(mTopProperty.value));
                    getValidLeftTopPoint(node, point);
                    logger.info(node.namedProperties.get("text:mText").value + " left:" + point.x + " top:" + point.y + " width:" + node.width + " height:" + node.height);
                    return new Rectangle(point.x, point.y, node.width, node.height);
                }
            }
        }
        return null;
    }

    private void searchElementRecursionByText(ViewNode viewNode, String text, ArrayList<ViewNode> resultNodes, int index) {
        if (viewNode.children.size() > 0) {
            for (int i = 0; i < viewNode.children.size(); i++) {
                ViewNode node = viewNode.children.get(i);
                ViewNode.Property property = node.namedProperties.get("text:mText");
                if (property == null) {
                    searchElementRecursionByText(node, text, resultNodes, index);
                } else {
                    if (!property.value.contains(text)) {
                        searchElementRecursionByText(node, text, resultNodes, index);
                    } else {
                        resultNodes.add(node);
                        if (resultNodes.size() > index) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private void searchElementRecursionById(ViewNode viewNode, String id, ArrayList<ViewNode> resultNodes) {
        if (viewNode.children.size() > 0) {
            for (int i = 0; i < viewNode.children.size(); i++) {
                ViewNode node = viewNode.children.get(i);
                ViewNode.Property property = node.namedProperties.get("mID");
                if (property == null) {
                    searchElementRecursionById(node, id, resultNodes);
                } else {
                    if (!property.value.equalsIgnoreCase(id)) {
                        searchElementRecursionById(node, id, resultNodes);
                    } else {
                        resultNodes.add(node);
                        return;
                    }
                }
            }
        }
    }

    private void getValidLeftTopPoint(ViewNode viewNode, Point point) {
        if (viewNode.parent == null) {
            return;
        }
        ViewNode parentNode = viewNode.parent;
        ViewNode.Property mLeftProperty = parentNode.namedProperties.get("layout:mLeft");
        ViewNode.Property mTopProperty = parentNode.namedProperties.get("layout:mTop");
        point.x = point.x + Integer.parseInt(mLeftProperty.value);
        point.y = point.y + Integer.parseInt(mTopProperty.value);
        getValidLeftTopPoint(parentNode, point);
    }


}
