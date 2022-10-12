FROM alpine:3.8

RUN apk update
RUN apk add --no-cache curl

RUN mkdir /code

COPY . /code
WORKDIR /code

RUN ["chmod", "+x", "process_delete_history_docdb.sh"]

ENTRYPOINT ["sh", "process_delete_history_docdb.sh" ]