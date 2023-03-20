PROJECT(drm-vncserver)
CMAKE_MINIMUM_REQUIRED(VERSION 2.6)

set(EXTRA_FLAGS "-I/usr/include/drm")

FILE(GLOB SOURCES src/*.c)
ADD_EXECUTABLE(drm-vncserver ${SOURCES})


find_package(PkgConfig)
pkg_check_modules(DRM REQUIRED libdrm)

if(NOT DRM_FOUND)
	message(FATAL_ERROR "libdrm(-dev) not found")
	return()
endif()

# LIBVNC
find_library(LIBVNC NAMES libvncserver vncserver libdrm)

#DRM
option(ENABLE_POLKIT "Use pkexec for elevated drmsend privileges" ON)

if (ENABLE_POLKIT)
	message(STATUS "Using Polkit/pkexec for elevating drmsend privileges")
	target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE USE_PKEXEC)
else()
	message(WARNING "Polkit support is disabled. You'll need to manually run `sudo setcap cap_sys_admin+ep \"${OBS_PLUGIN_DESTINATION}/obs-plugins/linux-kmsgrab-send\"` to allow it to grab framebuffers")
endif()

target_include_directories(drm-vncserver PRIVATE ${DRM_INCLUDE_DIRS})
target_link_libraries(drm-vncserver ${DRM_LDFLAGS} ${DRM_LIBRARIES} ${LIBVNC})
target_compile_options(drm-vncserver PRIVATE ${DRM_CFLAGS})

INSTALL(TARGETS drm-vncserver RUNTIME DESTINATION bin)