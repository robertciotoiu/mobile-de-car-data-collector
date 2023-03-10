FROM rabbitmq:3.11.9-management-alpine

# Enable the management console by default
RUN rabbitmq-plugins enable --offline rabbitmq_management

# Expose the default RabbitMQ port
EXPOSE 5672

# Expose the management console port
EXPOSE 15672

# Start RabbitMQ
CMD ["rabbitmq-server"]