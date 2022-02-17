@rem Remove the current hands.src.main.res dir symlinks that are for Linux and macOS and recreate hands.src.main.res dir symlinks for Windows.
@rem This script needs administrator permission. Must run this script as administrator.

@rem for hands example app.
cd /d %~dp0
cd hands\src\main
rm hands.src.main.res
mklink /d hands.src.main.res ..\..\..\hands.src.main.res

@rem for facemesh example app.
cd /d %~dp0
cd facemesh\src\main
rm hands.src.main.res
mklink /d hands.src.main.res ..\..\..\hands.src.main.res

@rem for face detection example app.
cd /d %~dp0
cd facedetection\src\main
rm hands.src.main.res
mklink /d hands.src.main.res ..\..\..\hands.src.main.res

dir
pause
