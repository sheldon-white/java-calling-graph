#!/bin/sh

(/usr/bin/git  --git-dir=$1 --no-pager log -M --follow -p -- $2) | grep -e '^[ADdcr@]'

