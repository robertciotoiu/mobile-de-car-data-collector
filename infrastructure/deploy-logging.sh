helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

# Cleanup
helm uninstall loki --namespace=loki
kubectl delete namespace loki

# TODO: remove if not needed
#kubectl delete psp loki -n loki
#kubectl delete clusterrole loki-grafana-clusterrole
#kubectl delete clusterrole loki-promtail
kubectl delete -n loki pvc loki-pvc
kubectl delete -n loki pv loki-pv

# Prepare namespace and PVC
kubectl create namespace loki
kubectl apply -f kubernetes/mobile-de-loki-pv.yaml -n loki
kubectl apply -f kubernetes/mobile-de-loki-pvc.yaml -n loki

# Install
helm upgrade --install loki --namespace=loki -f kubernetes/loki-values.yaml grafana/loki-stack --set grafana.enabled=true

# Get grafana admin password
kubectl get secret --namespace loki loki-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
sleep 10s
# Port-forward to make grafana accessible
kubectl port-forward --namespace loki service/loki-grafana 3000:80