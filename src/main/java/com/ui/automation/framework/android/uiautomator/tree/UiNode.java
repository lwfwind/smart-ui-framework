package com.ui.automation.framework.android.uiautomator.tree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Ui node.
 */
public class UiNode extends BasicTreeNode {
    private static final Pattern BOUNDS_PATTERN = Pattern
            .compile("\\[-?(\\d+),-?(\\d+)\\]\\[-?(\\d+),-?(\\d+)\\]");
    // use LinkedHashMap to preserve the order of the attributes
    private final Map<String, String> mAttributes = new LinkedHashMap<String, String>();
    private String mDisplayName = "ShouldNotSeeMe";
    private Object[] mCachedAttributesArray;

    /**
     * Add atrribute.
     *
     * @param key   the key
     * @param value the value
     */
    public void addAtrribute(String key, String value) {
        mAttributes.put(key, value);
        updateDisplayName();
        if ("bounds".equals(key)) {
            updateBounds(value);
        }
    }

    /**
     * Gets attributes.
     *
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(mAttributes);
    }

    /**
     * Builds the display name based on attributes of the node
     */
    private void updateDisplayName() {
        String className = mAttributes.get("class");
        if (className == null)
            return;
        String text = mAttributes.get("text");
        if (text == null)
            return;
        String contentDescription = mAttributes.get("content-desc");
        if (contentDescription == null)
            return;
        String index = mAttributes.get("index");
        if (index == null)
            return;
        String bounds = mAttributes.get("bounds");
        if (bounds == null) {
            return;
        }
        // shorten the standard class names, otherwise it takes up too much
        // space on UI
        className = className.replace("android.widget.", "");
        className = className.replace("android.view.", "");
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(index);
        builder.append(") ");
        builder.append(className);
        if (!text.isEmpty()) {
            builder.append(':');
            builder.append(text);
        }
        if (!contentDescription.isEmpty()) {
            builder.append(" {");
            builder.append(contentDescription);
            builder.append('}');
        }
        builder.append(' ');
        builder.append(bounds);
        mDisplayName = builder.toString();
    }

    private void updateBounds(String bounds) {
        Matcher m = BOUNDS_PATTERN.matcher(bounds);
        if (m.matches()) {
            x = Integer.parseInt(m.group(1));
            y = Integer.parseInt(m.group(2));
            width = Integer.parseInt(m.group(3)) - x;
            height = Integer.parseInt(m.group(4)) - y;
            mHasBounds = true;
        } else {
            throw new RuntimeException("Invalid bounds: " + bounds);
        }
    }

    @Override
    public String toString() {
        return mDisplayName;
    }

    /**
     * Gets attribute.
     *
     * @param key the key
     * @return the attribute
     */
    public String getAttribute(String key) {
        return mAttributes.get(key);
    }

    @Override
    public Object[] getAttributesArray() {
        // this approach means we do not handle the situation where an attribute
        // is added
        // after this function is first called. This is currently not a concern
        // because the
        // tree is supposed to be readonly
        if (mCachedAttributesArray == null) {
            mCachedAttributesArray = new Object[mAttributes.size()];
            int i = 0;
            for (String attr : mAttributes.keySet()) {
                mCachedAttributesArray[i++] = new AttributePair(attr,
                        mAttributes.get(attr));
            }
        }
        return mCachedAttributesArray;
    }

    /**
     * Gets xpath.
     *
     * @return the xpath
     */
    public String getXpath() {
        /*String className = getNodeClassAttribute();
        String xpath = "//" + className;
		String text = getAttribute("text");
		if (text != null && !text.equals("")) {
			xpath += "[@text='" + text + "']";
			return xpath;
		} else {
			return getAttribute("content-desc") != "" ? xpath
					+ "[@content-desc='" + getAttribute("content-desc") + "']"
					: xpath + "[@index='" + getAttribute("index") + "']";
		}*/
        String className = getNodeClassAttribute();
        String xpath = "/" + className;
        boolean flag = false;
        //------------------------------
        String text = getAttribute("text");
        if (text != null && !text.equals("")) {
            text = text.replaceAll("\"", "\\\\\"");
            xpath += "[@text=\\\"" + text + "\\\"";
            flag = true;
        }
        String content_desc = getAttribute("content-desc");
        if (!content_desc.equals("")) {
            content_desc = content_desc.replaceAll("'", "\\\\'");
            if (flag) {
                xpath += " and @content-desc=\\\"" + content_desc + "\\\"";
            } else {
                xpath += "[@content-desc=\\\"" + content_desc + "\\\"";
                flag = true;
            }
        }
        //-------------------------------
        /*String index = getAttribute("index");
        if(!index.equals("")){
			if(flag){
				xpath += " and @index=\\\"" + index + "\\\"";
			}else{
				xpath += "[@index=\\\"" + index + "\\\"";
				flag = true;
			}
		}*/
        if (flag) {
            xpath = xpath + "]";
        }
        return xpath;
    }

    /**
     * Gets xpath 2.
     *
     * @return the xpath 2
     */
    public String getXpath2() {
        /*String className = getNodeClassAttribute();
        String xpath = "//" + className;
		String text = getAttribute("text");
		if (text != null && !text.equals("")) {
			xpath += "[@text='" + text + "']";
			return xpath;
		} else {
			return getAttribute("content-desc") != "" ? xpath
					+ "[@content-desc='" + getAttribute("content-desc") + "']"
					: xpath + "[@index='" + getAttribute("index") + "']";
		}*/
        String className = getNodeClassAttribute();
        String xpath = "/" + className;
        boolean flag = false;
        //------------------------------
        String text = getAttribute("text");
        if (text != null && !text.equals("")) {
            text = text.replaceAll("\"", "\\\\\"");
            xpath += "[@text=\\\"" + text + "\\\"";
            flag = true;
        }
        String content_desc = getAttribute("content-desc");
        if (!content_desc.equals("")) {
            content_desc = content_desc.replaceAll("'", "\\\\'");
            if (flag) {
                xpath += " and @content-desc=\\\"" + content_desc + "\\\"";
            } else {
                xpath += "[@content-desc=\\\"" + content_desc + "\\\"";
                flag = true;
            }
        }
        //-------------------------------
        String index = getAttribute("index");
        if (!index.equals("")) {
            if (flag) {
                xpath += " and @index=\\\"" + index + "\\\"";
            } else {
                xpath += "[@index=\\\"" + index + "\\\"";
                flag = true;
            }
        }
        if (flag) {
            xpath = xpath + "]";
        }
        return xpath;
    }

    private String getNodeClassAttribute() {
        return this.mAttributes.get("class");
    }
}
