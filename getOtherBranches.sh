#!/bin/bash
url_git='https://github.com/vsaulnie/SOA_PI_Community_Detection.git'

ms_name='Launcher_MS'
mkdir ../$ms_name 
git clone $url_git -b $ms_name ../$ms_name

ms_name='Discovery_MS'
mkdir ../$ms_name 
git clone $url_git -b $ms_name ../$ms_name

ms_name='Giraph_API_MS'
mkdir ../$ms_name 
git clone $url_git -b $ms_name ../$ms_name

ms_name='Storer_MS'
mkdir ../$ms_name 
git clone $url_git -b $ms_name ../$ms_name


