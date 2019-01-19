.PHONY: build generate-docs

IMAGE_NAME=jordyv/kocam

build:
	docker build -t ${IMAGE_NAME} .
