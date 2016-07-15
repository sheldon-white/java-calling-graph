#!/bin/sh

(/usr/bin/git  --git-dir=$1 --no-pager log --follow -p -- $2) | grep -e '^[ADdc@]'

