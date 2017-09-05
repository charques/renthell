FROM nginx
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/nginx.conf

ADD build/es6-unbundled /usr/share/nginx/html/
ADD build/es6-unbundled /var/www/html/

EXPOSE 90
CMD ["nginx", "-g", "daemon off;"]