cmake_minimum_required(VERSION 3.22.1)

project("dracodemo")

add_library(
        dracodemo
        SHARED
        dracodemo.cpp)

find_library(log-lib log)

target_link_libraries(
        dracodemo
        ${log-lib})
