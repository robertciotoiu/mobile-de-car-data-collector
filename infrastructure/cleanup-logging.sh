helm uninstall loki --namespace=loki
kubectl delete namespace loki
kubectl delete -n loki pvc loki-pvc
kubectl delete -n loki pv loki-pv