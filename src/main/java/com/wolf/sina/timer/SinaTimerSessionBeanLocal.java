package com.wolf.sina.timer;

import javax.ejb.Local;

/**
 *
 * @author jianying9
 */
@Local
public interface SinaTimerSessionBeanLocal {
    
    public void refreshSpiderUserCookie();
    
    public void checkUpdateSinaUserTask();
    
    public void saveSinaUserNumPerHour();
}
