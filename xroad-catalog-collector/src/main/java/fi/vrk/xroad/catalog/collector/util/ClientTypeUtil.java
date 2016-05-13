/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.util;


import fi.vrk.xroad.catalog.collector.wsimport.ClientType;

/**
 * Created by sjk on 26.2.2016.
 */
public class ClientTypeUtil {

    private ClientTypeUtil() {
        // Private empty constructor
    }

    public static String toString(ClientType c) {
        StringBuilder sb = new StringBuilder("");
        sb.append("ObjectType: ");
        sb.append(c.getId().getObjectType());
        sb.append(" Name: ");
        sb.append(c.getName());
        sb.append(" XRoadInstance: ");
        sb.append(c.getId().getXRoadInstance());
        sb.append(" MemberClass: ");
        sb.append(c.getId().getMemberClass());
        sb.append(" MemberCode: ");
        sb.append(c.getId().getMemberCode());
        sb.append(" SubsystemCode: ");
        sb.append(c.getId().getSubsystemCode());
        return sb.toString();
    }

    public static String toString(fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType c) {
        StringBuilder sb = new StringBuilder("");
        sb.append("ObjectType: ");
        sb.append(c.getObjectType());
        sb.append(" XRoadInstance: ");
        sb.append(c.getXRoadInstance());
        sb.append(" MemberClass: ");
        sb.append(c.getMemberClass());
        sb.append(" MemberCode: ");
        sb.append(c.getMemberCode());
        sb.append(" SubsystemCode: ");
        sb.append(c.getSubsystemCode());
        return sb.toString();
    }
}
