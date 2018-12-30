package com.raptware.docdigitales;

public final class JNI {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native void StartDataBase(String FileName);
    public static native void CloseDataBase();
    public static native boolean CreateRegister(String FullName, String Email, String RFC, String EnterpriseName, String Password);
    public static native int CreateBranch(String UserEmail, String BranchName, String Street, String Colony,String Number, String PostalCode, String City, String Country);
    public static native boolean SaveBranch(String id, String BranchName, String Street, String Colony,String Number, String PostalCode, String City, String Country);
    public static native boolean Login(String Email, String Password);
    public static native String GetUserInfo(String Email);
    public static native String GetBranchesByUser(String Email);
    public static native String GetBranchByID(String id);
}
