#!/bin/bash
spring init --build=maven \
--java-version=17 \
--dependencies=data-jpa,mysql,lombok \
--packaging=jar \
--artifactId=svg-generator \
--groupId=com.ec.svg.generator \
--description="SVG Generation/Manipulation Engine" \
--name="SVG Generator" \
sample-app.zip