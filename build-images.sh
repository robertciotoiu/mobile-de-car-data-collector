//TODO: improve
cd ~/mobile-de-car-data-collector/mobile-de-crawler/
docker build -t com.robertciotoiu/mobile-de-crawler:latest .
docker tag com.robertciotoiu/mobile-de-crawler:latest localhost:5000/com.robertciotoiu/mobile-de-crawler:latest
docker push localhost:5000/com.robertciotoiu/mobile-de-crawler:latest
cd ~/mobile-de-car-data-collector/kubernetes
kubectl -n rc apply -f mobile-de-crawler.yaml