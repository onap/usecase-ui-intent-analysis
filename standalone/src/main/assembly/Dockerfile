FROM nexus3.onap.org:10001/onap/integration-java17:12.0.0

USER root

RUN mkdir /home/uui

ADD usecase-ui-intent-analysis-*-linux64.tar.gz /home/uui/

WORKDIR /home/uui

EXPOSE 8083

RUN addgroup -S uui && adduser -S uui -G uui && chown -R uui:uui /home/uui

USER uui
WORKDIR /home/uui
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar usecase-ui-intent-analysis.jar"]
