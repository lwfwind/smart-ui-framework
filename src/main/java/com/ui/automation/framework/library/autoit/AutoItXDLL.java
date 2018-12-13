package com.ui.automation.framework.library.autoit;

import com.ui.automation.framework.config.ProjectEnvironment;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.WString;

import java.util.List;

/**
 * The interface Auto it xdll.
 */
public interface AutoItXDLL extends com.sun.jna.Library {

    /**
     * The constant INSTANCE.
     */
    AutoItXDLL INSTANCE = (AutoItXDLL) Native.loadLibrary(
            System.getProperty("os.arch").equals("x86") ? ProjectEnvironment
                    .autoItXFile() : ProjectEnvironment.autoItX64File(),
            AutoItXDLL.class);
    /**
     * The constant AU3_INTDEFAULT.
     */
    public static int AU3_INTDEFAULT = -2147483647;

    /**
     * Au 3 init.
     */
    public void AU3_Init();

    /**
     * Au 3 error int.
     *
     * @return the int
     */
    public int AU3_error();

    /**
     * Au 3 auto it set option int.
     *
     * @param szOption the sz option
     * @param nValue   the n value
     * @return the int
     */
    public int AU3_AutoItSetOption(WString szOption, int nValue);

    /**
     * Au 3 block input.
     *
     * @param nFlag the n flag
     */
    public void AU3_BlockInput(int nFlag); // 1 = disable user input, 0 enable

    /**
     * Au 3 cd tray.
     *
     * @param szDrive  the sz drive
     * @param szAction the sz action
     */
    public void AU3_CDTray(WString szDrive, WString szAction); // drive: ,"open"
    // user input (to have auto it run
    // without interference)

    /**
     * Au 3 clip get.
     *
     * @param szClip   the sz clip
     * @param nBufSize the n buf size
     */
    public void AU3_ClipGet(byte[] szClip, int nBufSize);
    // or "closed"

    /**
     * Au 3 clip put.
     *
     * @param szClip the sz clip
     */
    public void AU3_ClipPut(WString szClip);

    /**
     * Au 3 control click int.
     *
     * @param szTitle    the sz title
     * @param szText     the sz text
     * @param szControl  the sz control
     * @param szButton   the sz button
     * @param nNumClicks the n num clicks
     * @param nX         the n x
     * @param nY         the n y
     * @return the int
     */
    public int AU3_ControlClick(WString szTitle, WString szText,
                                WString szControl, WString szButton, int nNumClicks, int nX, int nY);

    /**
     * Au 3 control command.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @param szCommand the sz command
     * @param szExtra   the sz extra
     * @param szResult  the sz result
     * @param nBufSize  the n buf size
     */
    public void AU3_ControlCommand(WString szTitle, WString szText,
                                   WString szControl, WString szCommand, WString szExtra,
                                   byte[] szResult, int nBufSize);

    /**
     * Au 3 control list view.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @param szCommand the sz command
     * @param szExtra1  the sz extra 1
     * @param szExtra2  the sz extra 2
     * @param szResult  the sz result
     * @param nBufSize  the n buf size
     */
    public void AU3_ControlListView(WString szTitle, WString szText,
                                    WString szControl, WString szCommand, WString szExtra1,
                                    WString szExtra2, byte[] szResult, int nBufSize);

    /**
     * Au 3 control disable int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlDisable(WString szTitle, WString szText,
                                  WString szControl);

    /**
     * Au 3 control enable int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlEnable(WString szTitle, WString szText,
                                 WString szControl);

    /**
     * Au 3 control focus int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlFocus(WString szTitle, WString szText,
                                WString szControl);

    /**
     * Au 3 control get focus.
     *
     * @param szTitle            the sz title
     * @param szText             the sz text
     * @param szControlWithFocus the sz control with focus
     * @param nBufSize           the n buf size
     */
    public void AU3_ControlGetFocus(WString szTitle, WString szText,
                                    byte[] szControlWithFocus, int nBufSize);

    /**
     * Au 3 control get handle.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_ControlGetHandle(WString szTitle, WString szText,
                                     WString szControl, byte[] szRetText, int nBufSize);

    /**
     * Au 3 control get pos x int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlGetPosX(WString szTitle, WString szText,
                                  WString szControl);

    /**
     * Au 3 control get pos y int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlGetPosY(WString szTitle, WString szText,
                                  WString szControl);

    /**
     * Au 3 control get pos height int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlGetPosHeight(WString szTitle, WString szText,
                                       WString szControl);

    /**
     * Au 3 control get pos width int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlGetPosWidth(WString szTitle, WString szText,
                                      WString szControl);

    /**
     * Au 3 control get text.
     *
     * @param szTitle       the sz title
     * @param szText        the sz text
     * @param szControl     the sz control
     * @param szControlText the sz control text
     * @param nBufSize      the n buf size
     */
    public void AU3_ControlGetText(WString szTitle, WString szText,
                                   WString szControl, byte[] szControlText, int nBufSize);

    /**
     * Au 3 control hide int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlHide(WString szTitle, WString szText,
                               WString szControl);

    /**
     * Au 3 control move int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @param nX        the n x
     * @param nY        the n y
     * @param nWidth    the n width
     * @param nHeight   the n height
     * @return the int
     */
    public int AU3_ControlMove(WString szTitle, WString szText,
                               WString szControl, int nX, int nY, int nWidth, int nHeight);

    /**
     * Au 3 control send int.
     *
     * @param szTitle    the sz title
     * @param szText     the sz text
     * @param szControl  the sz control
     * @param szSendText the sz send text
     * @param nMode      the n mode
     * @return the int
     */
    public int AU3_ControlSend(WString szTitle, WString szText,
                               WString szControl, WString szSendText, int nMode);

    /**
     * Au 3 control set text int.
     *
     * @param szTitle       the sz title
     * @param szText        the sz text
     * @param szControl     the sz control
     * @param szControlText the sz control text
     * @return the int
     */
    public int AU3_ControlSetText(WString szTitle, WString szText,
                                  WString szControl, WString szControlText);

    /**
     * Au 3 control show int.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szControl the sz control
     * @return the int
     */
    public int AU3_ControlShow(WString szTitle, WString szText,
                               WString szControl);

    /**
     * Au 3 drive map add.
     *
     * @param szDevice the sz device
     * @param szShare  the sz share
     * @param nFlags   the n flags
     * @param szUser   the sz user
     * @param szPwd    the sz pwd
     * @param szResult the sz result
     * @param nBufSize the n buf size
     */
    public void AU3_DriveMapAdd(WString szDevice, WString szShare, int nFlags,
                                WString szUser, WString szPwd, byte[] szResult, int nBufSize);

    /**
     * Au 3 drive map del int.
     *
     * @param szDevice the sz device
     * @return the int
     */
    public int AU3_DriveMapDel(WString szDevice);

    /**
     * Au 3 drive map get.
     *
     * @param szDevice  the sz device
     * @param szMapping the sz mapping
     * @param nBufSize  the n buf size
     */
    public void AU3_DriveMapGet(WString szDevice, byte[] szMapping, int nBufSize);

    /**
     * Au 3 ini delete int.
     *
     * @param szFilename the sz filename
     * @param szSection  the sz section
     * @param szKey      the sz key
     * @return the int
     */
    public int AU3_IniDelete(WString szFilename, WString szSection,
                             WString szKey);

    /**
     * Au 3 ini read.
     *
     * @param szFilename the sz filename
     * @param szSection  the sz section
     * @param szKey      the sz key
     * @param szDefault  the sz default
     * @param szValue    the sz value
     * @param nBufSize   the n buf size
     */
    public void AU3_IniRead(WString szFilename, WString szSection,
                            WString szKey, WString szDefault, byte[] szValue, int nBufSize);

    /**
     * Au 3 ini write int.
     *
     * @param szFilename the sz filename
     * @param szSection  the sz section
     * @param szKey      the sz key
     * @param szValue    the sz value
     * @return the int
     */
    public int AU3_IniWrite(WString szFilename, WString szSection,
                            WString szKey, WString szValue);

    /**
     * Au 3 is admin int.
     *
     * @return the int
     */
    public int AU3_IsAdmin();

    /**
     * Au 3 mouse click int.
     *
     * @param szButton the sz button
     * @param nX       the n x
     * @param nY       the n y
     * @param nClicks  the n clicks
     * @param nSpeed   the n speed
     * @return the int
     */
    public int AU3_MouseClick(WString szButton, int nX, int nY, int nClicks,
                              int nSpeed);

    /**
     * Au 3 mouse click drag int.
     *
     * @param szButton the sz button
     * @param nX1      the n x 1
     * @param nY1      the n y 1
     * @param nX2      the n x 2
     * @param nY2      the n y 2
     * @param nSpeed   the n speed
     * @return the int
     */
    public int AU3_MouseClickDrag(WString szButton, int nX1, int nY1, int nX2,
                                  int nY2, int nSpeed);

    /**
     * Au 3 mouse down.
     *
     * @param szButton the sz button
     */
    public void AU3_MouseDown(WString szButton);

    /**
     * Au 3 mouse get cursor int.
     *
     * @return the int
     */
    public int AU3_MouseGetCursor();

    /**
     * Au 3 mouse get pos x int.
     *
     * @return the int
     */
    public int AU3_MouseGetPosX();

    /**
     * Au 3 mouse get pos y int.
     *
     * @return the int
     */
    public int AU3_MouseGetPosY();

    /**
     * Au 3 mouse move int.
     *
     * @param nX     the n x
     * @param nY     the n y
     * @param nSpeed the n speed
     * @return the int
     */
    public int AU3_MouseMove(int nX, int nY, int nSpeed);

    /**
     * Au 3 mouse up.
     *
     * @param szButton the sz button
     */
    public void AU3_MouseUp(WString szButton);

    /**
     * Au 3 mouse wheel.
     *
     * @param szDirection the sz direction
     * @param nClicks     the n clicks
     */
    public void AU3_MouseWheel(WString szDirection, int nClicks);

    /**
     * Au 3 opt int.
     *
     * @param szOption the sz option
     * @param nValue   the n value
     * @return the int
     */
    public int AU3_Opt(WString szOption, int nValue);

    /**
     * Au 3 pixel checksum int.
     *
     * @param nLeft   the n left
     * @param nTop    the n top
     * @param nRight  the n right
     * @param nBottom the n bottom
     * @param nStep   the n step
     * @return the int
     */
    public int AU3_PixelChecksum(int nLeft, int nTop, int nRight, int nBottom,
                                 int nStep);

    /**
     * Au 3 pixel get color int.
     *
     * @param nX the n x
     * @param nY the n y
     * @return the int
     */
    public int AU3_PixelGetColor(int nX, int nY);

    /**
     * Au 3 pixel search.
     *
     * @param nLeft        the n left
     * @param nTop         the n top
     * @param nRight       the n right
     * @param nBottom      the n bottom
     * @param nCol         the n col
     * @param nVar         the n var
     * @param nStep        the n step
     * @param pPointResult the p point result
     */
    public void AU3_PixelSearch(int nLeft, int nTop, int nRight, int nBottom,
                                int nCol, int nVar, int nStep, LPPOINT pPointResult);

    /**
     * Au 3 process close int.
     *
     * @param szProcess the sz process
     * @return the int
     */
    public int AU3_ProcessClose(WString szProcess);

    /**
     * Au 3 process exists int.
     *
     * @param szProcess the sz process
     * @return the int
     */
    public int AU3_ProcessExists(WString szProcess);

    /**
     * Au 3 process set priority int.
     *
     * @param szProcess the sz process
     * @param nPriority the n priority
     * @return the int
     */
    public int AU3_ProcessSetPriority(WString szProcess, int nPriority);

    /**
     * Au 3 process wait int.
     *
     * @param szProcess the sz process
     * @param nTimeout  the n timeout
     * @return the int
     */
    public int AU3_ProcessWait(WString szProcess, int nTimeout);

    /**
     * Au 3 process wait close int.
     *
     * @param szProcess the sz process
     * @param nTimeout  the n timeout
     * @return the int
     */
    public int AU3_ProcessWaitClose(WString szProcess, int nTimeout);

    /**
     * Au 3 reg delete key int.
     *
     * @param szKeyname the sz keyname
     * @return the int
     */
    public int AU3_RegDeleteKey(WString szKeyname);

    /**
     * Au 3 reg delete val int.
     *
     * @param szKeyname   the sz keyname
     * @param szValuename the sz valuename
     * @return the int
     */
    public int AU3_RegDeleteVal(WString szKeyname, WString szValuename);

    /**
     * Au 3 reg enum key.
     *
     * @param szKeyname the sz keyname
     * @param nInstance the n instance
     * @param szResult  the sz result
     * @param nBufSize  the n buf size
     */
    public void AU3_RegEnumKey(WString szKeyname, int nInstance,
                               byte[] szResult, int nBufSize);

    /**
     * Au 3 reg enum val.
     *
     * @param szKeyname the sz keyname
     * @param nInstance the n instance
     * @param szResult  the sz result
     * @param nBufSize  the n buf size
     */
    public void AU3_RegEnumVal(WString szKeyname, int nInstance,
                               byte[] szResult, int nBufSize);

    /**
     * Au 3 reg read.
     *
     * @param szKeyname   the sz keyname
     * @param szValuename the sz valuename
     * @param szRetText   the sz ret text
     * @param nBufSize    the n buf size
     */
    public void AU3_RegRead(WString szKeyname, WString szValuename,
                            byte[] szRetText, int nBufSize);

    /**
     * Au 3 reg write int.
     *
     * @param szKeyname   the sz keyname
     * @param szValuename the sz valuename
     * @param szType      the sz type
     * @param szValue     the sz value
     * @return the int
     */
    public int AU3_RegWrite(WString szKeyname, WString szValuename,
                            WString szType, WString szValue);

    /**
     * Au 3 run int.
     *
     * @param szRun      the sz run
     * @param szDir      the sz dir
     * @param nShowFlags the n show flags
     * @return the int
     */
    public int AU3_Run(WString szRun, WString szDir, int nShowFlags);

    /**
     * Au 3 run as set int.
     *
     * @param szUser     the sz user
     * @param szDomain   the sz domain
     * @param szPassword the sz password
     * @param nOptions   the n options
     * @return the int
     */
    public int AU3_RunAsSet(WString szUser, WString szDomain,
                            WString szPassword, int nOptions);

    /**
     * Au 3 run wait int.
     *
     * @param szRun      the sz run
     * @param szDir      the sz dir
     * @param nShowFlags the n show flags
     * @return the int
     */
    public int AU3_RunWait(WString szRun, WString szDir, int nShowFlags);

    /**
     * Au 3 send.
     *
     * @param szSendText the sz send text
     * @param nMode      the n mode
     */
    public void AU3_Send(WString szSendText, int nMode);

    /**
     * Au 3 shutdown int.
     *
     * @param nFlags the n flags
     * @return the int
     */
    public int AU3_Shutdown(int nFlags);

    /**
     * Au 3 sleep.
     *
     * @param nMilliseconds the n milliseconds
     */
    public void AU3_Sleep(int nMilliseconds);

    /**
     * Au 3 statusbar get text.
     *
     * @param szTitle      the sz title
     * @param szText       the sz text
     * @param nPart        the n part
     * @param szStatusText the sz status text
     * @param nBufSize     the n buf size
     */
    public void AU3_StatusbarGetText(WString szTitle, WString szText,
                                     int nPart, byte[] szStatusText, int nBufSize);

    /**
     * Au 3 tool tip.
     *
     * @param szTip the sz tip
     * @param nX    the n x
     * @param nY    the n y
     */
    public void AU3_ToolTip(WString szTip, int nX, int nY);

    /**
     * Au 3 win active int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinActive(WString szTitle, WString szText);

    /**
     * Au 3 win activate.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     */
    public void AU3_WinActivate(WString szTitle, WString szText);

    /**
     * Au 3 win close int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinClose(WString szTitle, WString szText);

    /**
     * Au 3 win exists int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinExists(WString szTitle, WString szText);

    /**
     * Au 3 win get caret pos x int.
     *
     * @return the int
     */
    public int AU3_WinGetCaretPosX();

    /**
     * Au 3 win get caret pos y int.
     *
     * @return the int
     */
    public int AU3_WinGetCaretPosY();

    /**
     * Au 3 win get class list.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_WinGetClassList(WString szTitle, WString szText,
                                    byte[] szRetText, int nBufSize);

    /**
     * Au 3 win get client size height int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetClientSizeHeight(WString szTitle, WString szText);

    /**
     * Au 3 win get client size width int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetClientSizeWidth(WString szTitle, WString szText);

    /**
     * Au 3 win get handle.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_WinGetHandle(WString szTitle, WString szText,
                                 byte[] szRetText, int nBufSize);

    /**
     * Au 3 win get pos x int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetPosX(WString szTitle, WString szText);

    /**
     * Au 3 win get pos y int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetPosY(WString szTitle, WString szText);

    /**
     * Au 3 win get pos height int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetPosHeight(WString szTitle, WString szText);

    /**
     * Au 3 win get pos width int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetPosWidth(WString szTitle, WString szText);

    /**
     * Au 3 win get process.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_WinGetProcess(WString szTitle, WString szText,
                                  byte[] szRetText, int nBufSize);

    /**
     * Au 3 win get state int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinGetState(WString szTitle, WString szText);

    /**
     * Au 3 win get text.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_WinGetText(WString szTitle, WString szText,
                               byte[] szRetText, int nBufSize);

    /**
     * Au 3 win get title.
     *
     * @param szTitle   the sz title
     * @param szText    the sz text
     * @param szRetText the sz ret text
     * @param nBufSize  the n buf size
     */
    public void AU3_WinGetTitle(WString szTitle, WString szText,
                                byte[] szRetText, int nBufSize);

    /**
     * Au 3 win kill int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @return the int
     */
    public int AU3_WinKill(WString szTitle, WString szText);

    /**
     * Au 3 win menu select item int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @param szItem1 the sz item 1
     * @param szItem2 the sz item 2
     * @param szItem3 the sz item 3
     * @param szItem4 the sz item 4
     * @param szItem5 the sz item 5
     * @param szItem6 the sz item 6
     * @param szItem7 the sz item 7
     * @param szItem8 the sz item 8
     * @return the int
     */
    public int AU3_WinMenuSelectItem(WString szTitle, WString szText,
                                     WString szItem1, WString szItem2, WString szItem3, WString szItem4,
                                     WString szItem5, WString szItem6, WString szItem7, WString szItem8);

    /**
     * Au 3 win minimize all.
     */
    public void AU3_WinMinimizeAll();

    /**
     * Au 3 win minimize all undo.
     */
    public void AU3_WinMinimizeAllUndo();

    /**
     * Au 3 win move int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @param nX      the n x
     * @param nY      the n y
     * @param nWidth  the n width
     * @param nHeight the n height
     * @return the int
     */
    public int AU3_WinMove(WString szTitle, WString szText, int nX, int nY,
                           int nWidth, int nHeight);

    /**
     * Au 3 win set on top int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @param nFlag   the n flag
     * @return the int
     */
    public int AU3_WinSetOnTop(WString szTitle, WString szText, int nFlag);

    /**
     * Au 3 win set state int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @param nFlags  the n flags
     * @return the int
     */
    public int AU3_WinSetState(WString szTitle, WString szText, int nFlags);

    /**
     * Au 3 win set title int.
     *
     * @param szTitle    the sz title
     * @param szText     the sz text
     * @param szNewTitle the sz new title
     * @return the int
     */
    public int AU3_WinSetTitle(WString szTitle, WString szText,
                               WString szNewTitle);

    /**
     * Au 3 win set trans int.
     *
     * @param szTitle the sz title
     * @param szText  the sz text
     * @param nTrans  the n trans
     * @return the int
     */
    public int AU3_WinSetTrans(WString szTitle, WString szText, int nTrans);

    /**
     * Au 3 win wait int.
     *
     * @param szTitle  the sz title
     * @param szText   the sz text
     * @param nTimeout the n timeout
     * @return the int
     */
    public int AU3_WinWait(WString szTitle, WString szText, int nTimeout);

    /**
     * Au 3 win wait active int.
     *
     * @param szTitle  the sz title
     * @param szText   the sz text
     * @param nTimeout the n timeout
     * @return the int
     */
    public int AU3_WinWaitActive(WString szTitle, WString szText, int nTimeout);

    /**
     * Au 3 win wait close int.
     *
     * @param szTitle  the sz title
     * @param szText   the sz text
     * @param nTimeout the n timeout
     * @return the int
     */
    public int AU3_WinWaitClose(WString szTitle, WString szText, int nTimeout);

    /**
     * Au 3 win wait not active int.
     *
     * @param szTitle  the sz title
     * @param szText   the sz text
     * @param nTimeout the n timeout
     * @return the int
     */
    public int AU3_WinWaitNotActive(WString szTitle, WString szText,
                                    int nTimeout);

    /**
     * The type Lppoint.
     */
    public static class LPPOINT extends Structure {
        /**
         * The X.
         */
        public int x;
        /**
         * The Y.
         */
        public int y;

        @Override
        protected List getFieldOrder() {
            return null;
        }
    }

}
