/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qa.framework.android.hierarchyviewer;

import com.android.ddmlib.IDevice;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.android.hierarchyviewer.device.DeviceBridge;
import com.qa.framework.android.hierarchyviewer.device.DeviceBridge.ViewServerInfo;
import com.qa.framework.android.hierarchyviewer.device.ViewNode;
import com.qa.framework.android.hierarchyviewer.device.Window;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public abstract class HierarchyViewerHelper {
    private static final String TAG = "hierarchyviewer";
    private static Logger logger = Logger.getLogger(HierarchyViewerHelper.class);
    private static IDevice device = null;

    static {
        DebugBridge.init();
        try {
            device = DebugBridge.getDevice();
            if (device != null && device.isOnline()) {
                DeviceBridge.setupDeviceForward(device);
                ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
                if (viewServerInfo == null) {
                    throw new Exception("viewServerInfo is not correct!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            Rectangle rectangle = getElementLocationByText("15:20",2);
            if (rectangle != null) {
                logger.info("result left:" + rectangle.x + " top:" + rectangle.y + " width:" + rectangle.width + " height:" + rectangle.height);
            }

            logger.info(getElementTextById("id/editUsername"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    public static Point getElementCenterByText(String text, int index) {
        Rectangle rectangle = getElementLocationByText(text, index);
        if (rectangle != null) {
            return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        }
        return null;
    }

    public static Point getElementCenterByText(String text) {
        Rectangle rectangle = getElementLocationByText(text, 0);
        if (rectangle != null) {
            return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        }
        return null;
    }

    public static String getElementTextById(String id) {
        Window[] windows = DeviceBridge.loadWindows(device);
        for (Window window : windows) {
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


    public static Rectangle getElementLocationByText(String text, int index) {
        Window[] windows = DeviceBridge.loadWindows(device);
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

    private static void searchElementRecursionByText(ViewNode viewNode, String text, ArrayList<ViewNode> resultNodes, int index) {
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

    private static void searchElementRecursionById(ViewNode viewNode, String id, ArrayList<ViewNode> resultNodes) {
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

    private static void getValidLeftTopPoint(ViewNode viewNode, Point point) {
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
