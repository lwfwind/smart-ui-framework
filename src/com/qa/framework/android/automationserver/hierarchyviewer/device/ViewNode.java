/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.qa.framework.android.automationserver.hierarchyviewer.device;

import java.util.*;

/**
 * The type View node.
 */
public class ViewNode {

    /**
     * The constant MISCELLANIOUS.
     */
    public static final String MISCELLANIOUS = "miscellaneous";

    ;
    private static final double RED_THRESHOLD = 0.8;

    private static final double YELLOW_THRESHOLD = 0.5;
    /**
     * The Id.
     */
    public String id;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Hash code.
     */
    public String hashCode;
    /**
     * The Properties.
     */
    public List<Property> properties = new ArrayList<Property>();
    /**
     * The Named properties.
     */
    public Map<String, Property> namedProperties = new HashMap<String, Property>();
    /**
     * The Parent.
     */
    public ViewNode parent;
    /**
     * The Children.
     */
    public List<ViewNode> children = new ArrayList<ViewNode>();
    /**
     * The Left.
     */
    public int left;
    /**
     * The Top.
     */
    public int top;
    /**
     * The Width.
     */
    public int width;
    /**
     * The Height.
     */
    public int height;
    /**
     * The Scroll x.
     */
    public int scrollX;
    /**
     * The Scroll y.
     */
    public int scrollY;
    /**
     * The Padding left.
     */
    public int paddingLeft;
    /**
     * The Padding right.
     */
    public int paddingRight;
    /**
     * The Padding top.
     */
    public int paddingTop;
    /**
     * The Padding bottom.
     */
    public int paddingBottom;
    /**
     * The Margin left.
     */
    public int marginLeft;
    /**
     * The Margin right.
     */
    public int marginRight;
    /**
     * The Margin top.
     */
    public int marginTop;
    /**
     * The Margin bottom.
     */
    public int marginBottom;
    /**
     * The Baseline.
     */
    public int baseline;
    /**
     * The Will not draw.
     */
    public boolean willNotDraw;
    /**
     * The Has margins.
     */
    public boolean hasMargins;
    /**
     * The Has focus.
     */
    public boolean hasFocus;
    /**
     * The Index.
     */
    public int index;
    /**
     * The Measure time.
     */
    public double measureTime;
    /**
     * The Layout time.
     */
    public double layoutTime;
    /**
     * The Draw time.
     */
    public double drawTime;
    /**
     * The Measure rating.
     */
    public ProfileRating measureRating = ProfileRating.NONE;
    /**
     * The Layout rating.
     */
    public ProfileRating layoutRating = ProfileRating.NONE;
    /**
     * The Draw rating.
     */
    public ProfileRating drawRating = ProfileRating.NONE;
    /**
     * The Categories.
     */
    public Set<String> categories = new TreeSet<String>();
    /**
     * The Window.
     */
    public Window window;
    /**
     * The Image references.
     */
    public int imageReferences = 1;
    /**
     * The View count.
     */
    public int viewCount;
    /**
     * The Filtered.
     */
    public boolean filtered;
    /**
     * The Protocol version.
     */
    public int protocolVersion;

    /**
     * Instantiates a new View node.
     *
     * @param window the window
     * @param parent the parent
     * @param data   the data
     */
    public ViewNode(Window window, ViewNode parent, String data) {
        this.window = window;
        this.parent = parent;
        index = this.parent == null ? 0 : this.parent.children.size();
        if (this.parent != null) {
            this.parent.children.add(this);
        }
        int delimIndex = data.indexOf('@');
        name = data.substring(0, delimIndex);
        data = data.substring(delimIndex + 1);
        delimIndex = data.indexOf(' ');
        hashCode = data.substring(0, delimIndex);
        loadProperties(data.substring(delimIndex + 1).trim());

        measureTime = -1;
        layoutTime = -1;
        drawTime = -1;
    }

    /**
     * Reference image.
     */
    public void referenceImage() {
        imageReferences++;
    }

    private void loadProperties(String data) {
        int start = 0;
        boolean stop;
        do {
            int index = data.indexOf('=', start);
            ViewNode.Property property = new ViewNode.Property();
            property.name = data.substring(start, index);

            int index2 = data.indexOf(',', index + 1);
            int length = Integer.parseInt(data.substring(index + 1, index2));
            start = index2 + 1 + length;
            property.value = data.substring(index2 + 1, index2 + 1 + length);

            properties.add(property);
            namedProperties.put(property.name, property);

            stop = start >= data.length();
            if (!stop) {
                start += 1;
            }
        } while (!stop);

        Collections.sort(properties, new Comparator<ViewNode.Property>() {
            public int compare(ViewNode.Property source, ViewNode.Property destination) {
                return source.name.compareTo(destination.name);
            }
        });

        id = namedProperties.get("mID").value;

        left =
                namedProperties.containsKey("mLeft") ? getInt("mLeft", 0) : getInt("layout:mLeft",
                        0);
        top = namedProperties.containsKey("mTop") ? getInt("mTop", 0) : getInt("layout:mTop", 0);
        width =
                namedProperties.containsKey("getWidth()") ? getInt("getWidth()", 0) : getInt(
                        "layout:getWidth()", 0);
        height =
                namedProperties.containsKey("getHeight()") ? getInt("getHeight()", 0) : getInt(
                        "layout:getHeight()", 0);
        scrollX =
                namedProperties.containsKey("mScrollX") ? getInt("mScrollX", 0) : getInt(
                        "scrolling:mScrollX", 0);
        scrollY =
                namedProperties.containsKey("mScrollY") ? getInt("mScrollY", 0) : getInt(
                        "scrolling:mScrollY", 0);
        paddingLeft =
                namedProperties.containsKey("mPaddingLeft") ? getInt("mPaddingLeft", 0) : getInt(
                        "padding:mPaddingLeft", 0);
        paddingRight =
                namedProperties.containsKey("mPaddingRight") ? getInt("mPaddingRight", 0) : getInt(
                        "padding:mPaddingRight", 0);
        paddingTop =
                namedProperties.containsKey("mPaddingTop") ? getInt("mPaddingTop", 0) : getInt(
                        "padding:mPaddingTop", 0);
        paddingBottom =
                namedProperties.containsKey("mPaddingBottom") ? getInt("mPaddingBottom", 0)
                        : getInt("padding:mPaddingBottom", 0);
        marginLeft =
                namedProperties.containsKey("layout_leftMargin") ? getInt("layout_leftMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_leftMargin", Integer.MIN_VALUE);
        marginRight =
                namedProperties.containsKey("layout_rightMargin") ? getInt("layout_rightMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_rightMargin", Integer.MIN_VALUE);
        marginTop =
                namedProperties.containsKey("layout_topMargin") ? getInt("layout_topMargin",
                        Integer.MIN_VALUE) : getInt("layout:layout_topMargin", Integer.MIN_VALUE);
        marginBottom =
                namedProperties.containsKey("layout_bottomMargin") ? getInt("layout_bottomMargin",
                        Integer.MIN_VALUE)
                        : getInt("layout:layout_bottomMargin", Integer.MIN_VALUE);
        baseline =
                namedProperties.containsKey("getBaseline()") ? getInt("getBaseline()", 0) : getInt(
                        "layout:getBaseline()", 0);
        willNotDraw =
                namedProperties.containsKey("willNotDraw()") ? getBoolean("willNotDraw()", false)
                        : getBoolean("drawing:willNotDraw()", false);
        hasFocus =
                namedProperties.containsKey("hasFocus()") ? getBoolean("hasFocus()", false)
                        : getBoolean("focus:hasFocus()", false);

        hasMargins =
                marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
                        && marginTop != Integer.MIN_VALUE && marginBottom != Integer.MIN_VALUE;

        for (String name : namedProperties.keySet()) {
            int index = name.indexOf(':');
            if (index != -1) {
                categories.add(name.substring(0, index));
            }
        }
        if (categories.size() != 0) {
            categories.add(MISCELLANIOUS);
        }
    }

    /**
     * Sets profile ratings.
     */
    public void setProfileRatings() {
        final int N = children.size();
        if (N > 1) {
            double totalMeasure = 0;
            double totalLayout = 0;
            double totalDraw = 0;
            for (int i = 0; i < N; i++) {
                ViewNode child = children.get(i);
                totalMeasure += child.measureTime;
                totalLayout += child.layoutTime;
                totalDraw += child.drawTime;
            }
            for (int i = 0; i < N; i++) {
                ViewNode child = children.get(i);
                if (child.measureTime / totalMeasure >= RED_THRESHOLD) {
                    child.measureRating = ProfileRating.RED;
                } else if (child.measureTime / totalMeasure >= YELLOW_THRESHOLD) {
                    child.measureRating = ProfileRating.YELLOW;
                } else {
                    child.measureRating = ProfileRating.GREEN;
                }
                if (child.layoutTime / totalLayout >= RED_THRESHOLD) {
                    child.layoutRating = ProfileRating.RED;
                } else if (child.layoutTime / totalLayout >= YELLOW_THRESHOLD) {
                    child.layoutRating = ProfileRating.YELLOW;
                } else {
                    child.layoutRating = ProfileRating.GREEN;
                }
                if (child.drawTime / totalDraw >= RED_THRESHOLD) {
                    child.drawRating = ProfileRating.RED;
                } else if (child.drawTime / totalDraw >= YELLOW_THRESHOLD) {
                    child.drawRating = ProfileRating.YELLOW;
                } else {
                    child.drawRating = ProfileRating.GREEN;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            children.get(i).setProfileRatings();
        }
    }

    /**
     * Sets view count.
     */
    public void setViewCount() {
        viewCount = 1;
        final int N = children.size();
        for (int i = 0; i < N; i++) {
            ViewNode child = children.get(i);
            child.setViewCount();
            viewCount += child.viewCount;
        }
    }

    /**
     * Filter.
     *
     * @param text the text
     */
    public void filter(String text) {
        int dotIndex = name.lastIndexOf('.');
        String shortName = (dotIndex == -1) ? name : name.substring(dotIndex + 1);
        filtered =
                !text.equals("")
                        && (shortName.toLowerCase().contains(text.toLowerCase()) || (!id
                        .equals("NO_ID") && id.toLowerCase().contains(text.toLowerCase())));
        final int N = children.size();
        for (int i = 0; i < N; i++) {
            children.get(i).filter(text);
        }
    }

    private boolean getBoolean(String name, boolean defaultValue) {
        Property p = namedProperties.get(name);
        if (p != null) {
            try {
                return Boolean.parseBoolean(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getInt(String name, int defaultValue) {
        Property p = namedProperties.get(name);
        if (p != null) {
            try {
                return Integer.parseInt(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override
    public String toString() {
        return name + "@" + hashCode;
    }

    /**
     * The enum Profile rating.
     */
    public static enum ProfileRating {
        /**
         * Red profile rating.
         */
        RED,
        /**
         * Yellow profile rating.
         */
        YELLOW,
        /**
         * Green profile rating.
         */
        GREEN,
        /**
         * None profile rating.
         */
        NONE
    }

    /**
     * The type Property.
     */
    public static class Property {
        /**
         * The Name.
         */
        public String name;

        /**
         * The Value.
         */
        public String value;

        @Override
        public String toString() {
            return name + '=' + value;
        }
    }
}
