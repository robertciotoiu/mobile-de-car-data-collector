cd .. || exit
mvn clean install -Dmaven.test.skip
cd mobile-de-rabbitmq/ || exit
docker build -t localhost:5000/com.robertciotoiu/mobile-de-rabbitmq:latest .
docker push localhost:5000/com.robertciotoiu/mobile-de-rabbitmq:latest

cd .. || exit
cd mobile-de-crawler/ || exit
docker build -t localhost:5000/com.robertciotoiu/mobile-de-crawler:latest .
docker push localhost:5000/com.robertciotoiu/mobile-de-crawler:latest

cd .. || exit
cd mobile-de-scraper/ || exit
docker build -t localhost:5000/com.robertciotoiu/mobile-de-scraper:latest .
docker push localhost:5000/com.robertciotoiu/mobile-de-scraper:latest

cd .. || exit
cd infrastructure/kubernetes/ || exit


kubectl delete namespace rc
kubectl create namespace rc

#TODO: find a better wait to orchestrate startup order & ready awaits
kubectl -n rc apply -f mobile-de-mongodb.yaml
sleep 10s
kubectl -n rc apply -f mobile-de-rabbitmq.yaml
sleep 10s
kubectl -n rc apply -f mobile-de-crawler.yaml
kubectl -n rc apply -f mobile-de-scraper.yaml