#!/bin/sh
/wait && exec java -cp  app:app/lib/* ${JAVA_OPTS} \
 com.kshitijpatil.tazabazar.ApiServerApplication ${@} \
 --spring.profiles.active=prod