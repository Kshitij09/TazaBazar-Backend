FROM busybox:1.31.1-uclibc as builder
RUN wget -O /wait https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait &&\
    chmod +x /wait

FROM adoptopenjdk/openjdk11:alpine-slim
RUN addgroup -S tzb && adduser -S tzb-user -G tzb
USER tzb-user
COPY --from=builder --chown=tzb-user:tzb /wait /wait
ARG DEPENDENCY=target
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY entrypoint.sh /app/
ENTRYPOINT ["/app/entrypoint.sh"]