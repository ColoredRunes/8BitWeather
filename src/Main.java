import engine.App;

public class Main {
    public static void main(String[] args) {
        javafx.application.Application.launch(App.class, args);
    }
    //_______________________________________________________________________________________//
        // _________________________________APPLICATION_________________________________ //
        // __________________________________PACKAGING__________________________________ //
    //___________________________________________________________________   ____________________//
    /**
     * 

    ---[for internal notes]---<p>
    <pre>
    COMMANDS:

    dir /s /b src\*.java > sources.txt                  <p>
    javac -d out -cp "libs/*" @sources.txt
    jar cfm dist/app.jar MANIFEST.MF -C out .

    rmdir /s /q installer
    dir /s /b src\*.java > sources.txt
    javac -d out -cp "libs/*" @sources.txt
    jar cfm dist/app.jar MANIFEST.MF -C out .
    xcopy libs dist\libs /E /I /Y
    xcopy resources dist\resources /E /I /Y
    jpackage --name "8 Bit Weather" --input dist --main-jar app.jar --main-class Main --icon _launch4j\icon.ico --java-options "-Dapp.dir=$APPDIR" --type app-image --dest installer
    // adds shortcut request
    jpackage --app-image "installer\8 Bit Weather" --name "8 Bit Weather" --type exe --win-shortcut-prompt --win-menu --win-dir-chooser --win-per-user-install --dest setup
    </pre>
    MANIFEST.MF is a simple part of the ZIP (jar) which tells the JVM which class to use for main,
    also where the libraries are.
    
    
    */
    @SuppressWarnings("unused")
    private final void __useless__(){
        return;
    }
}
