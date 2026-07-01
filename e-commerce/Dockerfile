# =========================
# Build Stage
# =========================
FROM node:24-alpine AS BUILD

WORKDIR /app

# Declare build args
ARG VITE_API_URL
ARG VITE_STRIPE_PUBLIC_KEY

# Make them available as env vars during npm run build
ENV VITE_API_URL=$VITE_API_URL
ENV VITE_STRIPE_PUBLIC_KEY=$VITE_STRIPE_PUBLIC_KEY

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# =========================
# Production Stage
# =========================

FROM nginx:alpine

# Copy build output to Nginx
COPY --from=build /app/dist /usr/share/nginx/html


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]