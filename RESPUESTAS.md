##Aclaración, en el mismo directorio del archivo RESPUESTAS.md se ha incluido un archivio JSON con todas las peticiones a la API para ser importadas en el "Postman".

## Ejercicio 2, URLs:
	
	#Cuentas Corrientes 
	
	curl --location --request GET "http://localhost:8080/api/cuentaCorriente/listar"
	
	curl --location --request GET "http://localhost:8080/api/cuentaCorriente/eliminar/1"
	
	curl --location --request POST "http://localhost:8080/api/cuentaCorriente/crear" \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"isoCode" : 12501510,
		"numero": 1,
		"moneda": "PESOS",
		"saldo": 55.55,
		"titular" : {
			"rut" : "111211"
		}
	}'

	curl --location --request POST "http://localhost:8080/api/cuentaCorriente/crear" \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"isoCode" : 12501510,
		"numero": 1,
		"moneda": "PESOS",
		"saldo": 55.55,
		"titular" : {
			"id" : 1
		}
	}'
	
	#Movimientos 
	
	curl --location --request GET "http://localhost:8080/api/movimiento/listar/1"
	
	curl --location --request POST "http://localhost:8080/api/movimiento/agregar" \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"id": null,
		"fecha" : "2020-03-15T19:46:21.343959",
		"importe": 255.55,
	   "cuentaCorriente": {
			"id": 1
		}
	}'
	
	curl --location --request POST "http://localhost:8080/api/movimiento/agregar" \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"fecha" : "2020-03-15T19:46:21.343959",
		"descripcion" : "Descripcion del movimiento 2",
		"importe": 23225.55,
		"cuentaCorriente": {
			"id": 1
		}
	}'
	
	curl --location --request POST "http://localhost:8080/api/movimiento/agregar" \
	--header 'Content-Type: application/json' \
	--data-raw '{
		"fecha" : "2020-04-15T19:46:21.343959",
		"importe" : 25550.64,
		"descripcion" : "Descripcion del movimiento 3",
	   "cuentaCorriente": {
			"id": 1
		}
	}'
	