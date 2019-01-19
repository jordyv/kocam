.PHONY: build build-docker release

IMAGE_NAME=jordyv/kocam

build-docker:
	docker build -t ${IMAGE_NAME} .

build:
	./gradlew build

# Create tarball of dist
release:
	./gradlew installDist
	tar -C build/install -c -v -f release.tar kocam
