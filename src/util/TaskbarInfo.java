package util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

public class TaskbarInfo {
    public static RECT rect;

    public static int x;
    public static int y;
    public static int width;
    public static int height;
    
    public static void buildTaskbarInfo() {
        HWND taskbar = User32.INSTANCE.FindWindow("Shell_TrayWnd", null);

        if (taskbar == null) {
            System.out.println("Taskbar not found.");
            return;
        }

        rect = new RECT();
        User32.INSTANCE.GetWindowRect(taskbar, rect);

        x = rect.left;
        y = rect.top;
        width = rect.right - rect.left;
        height = rect.bottom - rect.top;

        if (rect.top == 0 && height < 200) {
            System.out.println("Taskbar is at the TOP.");
        } else if (rect.left == 0 && width < 200) {
            System.out.println("Taskbar is on the LEFT.");
        } else if (rect.right == java.awt.Toolkit.getDefaultToolkit().getScreenSize().width && width < 200) {
            System.out.println("Taskbar is on the RIGHT.");
        } else {
            System.out.println("Taskbar is at the BOTTOM.");
        }
    }
    public static int getHeight() {
        return height;
    }
    public static int getWidth() {
        return width;
    }
    public static int getX() {
        return x;
    }
    public static int getY() {
        return y;
    }
}
