# 设置构建本机库文件所需的 CMake 的最小版本
cmake_minimum_required(VERSION 3.4.1)

# 添加自己写的 C/C++ 源文件
add_library( # 设置库的名字
             native-lib

             # 设置可共享库
             SHARED

             # 设置源文件的相对路径
             src/main/cpp/native-lib.cpp )

# 依赖 NDK 中的库
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# 将目标库和 NDK 中的库链接
target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )