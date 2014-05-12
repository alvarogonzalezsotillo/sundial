/*
 * ScreenCoord.java
 *
 * Created on 14 fevrier 2007, 16:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package computation.sideralis.object;

/**
 *
 * @author Bernard
 */
public class ScreenCoord {
    /** X coordinate */
    public short x;
    /** Y coordinate */
    public short y;
    /** Is the object visible ? - Set as public to improve performances */
    public boolean visible;
        
    /** Creates a new instance of ScreenCoord */
    public ScreenCoord() {
        visible = true;
    }
}
