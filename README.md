# SSL Poke

Test SSL Connection using Java. Reference: [https://gist.github.com/4ndrej/4547029](https://gist.github.com/4ndrej/4547029)

## Installation

### 1. Deploy

Use Gradle to generate SSL Poke JAR file.

```shell
$ ./gradlew build
```

### 2. Run

To run SSL Poke JAR file, just execute the following line:

```shell
$ java -jar build/libs/ssl-poke.jar example.com 443
```

### 3. Generate Certificate

To generate certificate using openssl command. Reference: [https://unix.stackexchange.com/questions/368123/how-to-extract-the-root-ca-and-subordinate-ca-from-a-certificate-chain-in-linux](https://unix.stackexchange.com/questions/368123/how-to-extract-the-root-ca-and-subordinate-ca-from-a-certificate-chain-in-linux)

```shell
openssl s_client -showcerts -verify 5 -connect example.com:443 < /dev/null | awk '/BEGIN/,/END/{ if(/BEGIN/){a++}; out="cert"a".crt"; print >out}' && for cert in .crt; do newname=$(openssl x509 -noout -subject -in $cert | sed -n 's/^.*CN=\(.\)$/\1/; s/[ ,.*]//g; s///g; s/^_//g;p').pem; mv $cert $newname; done
```