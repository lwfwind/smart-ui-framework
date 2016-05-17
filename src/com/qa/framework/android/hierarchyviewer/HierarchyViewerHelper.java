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
            Rectangle rectangle = getElementLocationByText("15:40",2);
            if (rectangle != null) {
                logger.info("result left:" + rectangle.x + " top:" + rectangle.y + " width:" + rectangle.width + " height:" + rectangle.height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    public static Point getElementCenterByText(String text, int... index){
        Rectangle rectangle = getElementLocationByText(text,index);
        if (rectangle != null) {
            return new Point(rectangle.x+rectangle.width/2,rectangle.y+rectangle.height/2);
        }
        return null;
    }


    public static Rectangle getElementLocationByText(String text, int... index){
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
                    searchElementRecursion(viewNode,text,resultRectangles);
                    if(resultRectangles.size() > 0) {
                        return resultRectangles.get(index[0]);
                    }
                }
            }
        }
        return null;
    }

    private static void searchElementRecursion(ViewNode viewNode, String text, ArrayList<Rectangle> resultRectangles){
        if(viewNode.children.size() > 0) {
            //logger.info(viewNode.name);
            for (int i = 0; i < viewNode.children.size(); i++) {
                ViewNode node = viewNode.children.get(i);
                ViewNode.Property property = node.namedProperties.get("text:mText");
                if(property == null){
                    searchElementRecursion(node,text,resultRectangles);
                }
                else {
                    if (!property.value.contains(text)) {
                        searchElementRecursion(node, text,resultRectangles);
                    } else {
                        ViewNode.Property mLeftProperty = node.namedProperties.get("layout:mLeft");
                        ViewNode.Property mTopProperty = node.namedProperties.get("layout:mTop");
                        Point point = new Point(Integer.parseInt(mLeftProperty.value),Integer.parseInt(mTopProperty.value));
                        getValidLeftTopPoint(node,point);
                        logger.info(property.value + " left:" + point.x + " top:" + point.y + " width:" + node.width + " height:" + node.height);
                        resultRectangles.add(new Rectangle(point.x,point.y,node.width,node.height));
                    }
                }
            }
        }
    }

    private static void getValidLeftTopPoint(ViewNode viewNode, Point point){
        if(viewNode.parent == null){
            return;
        }
        ViewNode parentNode = viewNode.parent;
        ViewNode.Property mLeftProperty = parentNode.namedProperties.get("layout:mLeft");
        ViewNode.Property mTopProperty = parentNode.namedProperties.get("layout:mTop");
        point.x = point.x + Integer.parseInt(mLeftProperty.value);
        point.y = point.y + Integer.parseInt(mTopProperty.value);
        getValidLeftTopPoint(parentNode,point);
    }


}
