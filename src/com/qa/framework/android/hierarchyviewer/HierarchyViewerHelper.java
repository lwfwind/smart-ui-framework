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
import com.qa.framework.android.hierarchyviewer.device.ViewNode;
import com.qa.framework.android.hierarchyviewer.device.Window;
import com.qa.framework.android.hierarchyviewer.device.DeviceBridge.ViewServerInfo;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class HierarchyViewerHelper {
    private static Logger logger = Logger.getLogger(HierarchyViewerHelper.class);
    public static final String TAG = "hierarchyviewer";

    public static void main(String[] args) {
        DebugBridge.init();
        try {
            Rectangle rectangle = getNodeLocationByText("菲律宾老师");
            if (rectangle != null) {
                logger.info(rectangle.x);
                logger.info(rectangle.y);
                logger.info(rectangle.width);
                logger.info(rectangle.height);
            }
            rectangle = getNodeLocationByText("欧美老师");
            if (rectangle != null) {
                logger.info(rectangle.x);
                logger.info(rectangle.y);
                logger.info(rectangle.width);
                logger.info(rectangle.height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    public static Point getNodeCenterByText(String text){
        Rectangle rectangle = getNodeLocationByText(text);
        if (rectangle != null) {
            return new Point(rectangle.x+rectangle.width/2,rectangle.y+rectangle.height/2);
        }
        return null;
    }


    public static Rectangle getNodeLocationByText(String text){
        IDevice device = null;
        try {
            device = DebugBridge.getDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (device != null && device.isOnline()) {
            DeviceBridge.setupDeviceForward(device);
            ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
            if (viewServerInfo == null) {
                return null;
            }
            Window[] windows = DeviceBridge.loadWindows(device);
            //logger.info(windows.length);
            for (Window window : windows) {
                ViewNode viewNode = DeviceBridge.loadWindowData(window);
                if (viewNode != null) {
                    ArrayList<Rectangle> resultRectangles = new ArrayList<Rectangle>();
                    searchNodeRecursion(viewNode,text,resultRectangles);
                    if(resultRectangles.size() > 0) {
                        return resultRectangles.get(0);
                    }
                }
            }
        }
        return null;
    }

    private static void searchNodeRecursion(ViewNode viewNode, String text, ArrayList<Rectangle> resultRectangles){
        if(viewNode.children.size() > 0) {
            //logger.info(viewNode.name);
            for (int i = 0; i < viewNode.children.size(); i++) {
                ViewNode node = viewNode.children.get(i);
                ViewNode.Property property = node.namedProperties.get("text:mText");
                if(property == null){
                    searchNodeRecursion(node,text,resultRectangles);
                }
                else {
                    logger.info(property.value);
                    if (!property.value.contains(text)) {
                        searchNodeRecursion(node, text,resultRectangles);
                    } else {
                        ViewNode.Property mLeftProperty = node.namedProperties.get("layout:mLeft");
                        ViewNode.Property mTopProperty = node.namedProperties.get("layout:mTop");
                        if(mLeftProperty.value.equals("0") && mTopProperty.value.equals("0")){
                            Point point = getValidLeftTopPoint(node);
                            resultRectangles.add(new Rectangle(point.x,point.y,node.width,node.height));
                        }
                        else
                        {
                            resultRectangles.add(new Rectangle(Integer.parseInt(mLeftProperty.value),Integer.parseInt(mTopProperty.value),node.width,node.height));
                        }
                    }
                }
            }
        }
    }

    private static Point getValidLeftTopPoint(ViewNode viewNode){
        if(viewNode.parent == null){
            return new Point(0,0);
        }
        ViewNode parentNode = viewNode.parent;
        ViewNode.Property mLeftProperty = parentNode.namedProperties.get("layout:mLeft");
        ViewNode.Property mTopProperty = parentNode.namedProperties.get("layout:mTop");
        if(mLeftProperty.value.equals("0") && mTopProperty.value.equals("0")){
            return getValidLeftTopPoint(parentNode);
        }
        else
        {
            return new Point(Integer.parseInt(mLeftProperty.value),Integer.parseInt(mTopProperty.value));
        }
    }


}
