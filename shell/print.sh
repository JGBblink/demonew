#!/bin/bash

df -h | awk '$1 ~ /disk/ {print $0}'