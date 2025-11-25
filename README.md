Calc3D - GitHub-ready Android Compose project
-------------------------------------------

This project is prepared for uploading to GitHub and building via GitHub Actions.
- Includes workflow at .github/workflows/build.yml
- Gradle wrapper properties present (gradle-wrapper.jar is expected to be provided
  by Gradle or the gradle action).

How to use:
1. Upload entire folder contents to a new GitHub repository.
2. Push to branch 'main' or 'master' (or run workflow manually).
3. Check Actions tab, wait for 'Android Build (assembleDebug)' to finish.
4. Download artifact 'app-debug-apk' and install on your phone.
