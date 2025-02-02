**Documentation de l'API Marchand Distant**  

L’**API Marchand**  permet aux partenaires commerciaux d’intégrer un système de paiement sécurisé directement dans leurs applications ou sites web. Conçue pour initier des transactions sur primes, elle offre une flexibilité optimale tout en assurant la sécurité des données et la traçabilité des opérations.


### **Prérequis**  
1. Compte marchand admin validé sur la plateforme.  
2. Accès administrateur marchand pour générer les clés API.  
3. Serveur HTTPS pour recevoir les callbacks.  

---

## **Étapes d’Intégration**  

### **Étape 1 : Génération des Clés API**  
**Objectif** : Obtenir les identifiants nécessaires pour authentifier les requêtes. Ces identifiants sont envoyés à l'adresse e-mail de l'administrateur du marchand lors de la génération des clés API.

1. **Email format** :  
   ```
    "apiKey": "prime_api_key_550e8400...",  
    "apiSecret": "prime_api_secret_550e8400...",  
    "hmacKey": "aes256_encrypted_value"  
   ```  
3. **Actions** :  
   - Stockez `apiKey` et `apiSecret` bien sécurisé.  
   - La `hmacKey` sera utilisée pour vérifier les callbacks.  

---

### **Étape 2 : Initialisation d’un Paiement**  
**Objectif** : Démarrer une transaction et envoyer un lien de paiement au client.  

1. **Endpoint** :  
   ```bash  
   POST https://${base_url}/api/remote-merchant/payment-request
   ```  
2. **En-têtes Requis** :  
   ```http  
   X-API-KEY: "prime_api_key_550e8400..."  
   X-API-SECRET: "prime_api_secret_550e8400..."  
   Content-Type: application/json  
   ```  
3. **Corps de la Requête** :  
   ```json  
   {  
     "callbackUrl": "https://votre-site.com/callback",  
     "amount": 5000,  // Montant en unités (ex: 5000 XOF)  
     "phoneClient": "+221767760904",  
     "externalRefId": "CMD_20230920_123",  // Unique par transaction  
     "commandName": "Abonnement Premium"  
   }  
   ```  
4. **Réponse** :  
   ```json  
   {
     "status" : "SUCCEED",
     "message" : "Operation successful",
     "data" : {
       "paymentId" : "dab17de0-4ed8-4d4a-92d1-a6bba2491bd4",
       "urlPayment" : "https://www.dab17de0-4ed8-4d4a-92d1-a6bba2491bd4.prime",
       "shopName" : "Exclusive",
       "shopLogo" : "https://primepaysn.s3.eu-west-3.amazonaws.com/a591fc36-6c34-4515-a47b-01b9d444d4d6-d5633470-29a9-40b2-8123-95c112538895.png",
       "userMerchantSeller" : "Ousmane DIOP",
       "userCustomerBuyer" : "",
       "statusPaymentMerchant" : "INIT",
       "creditStatut" : "OPEN",
       "refundPayments" : [ {
         "name" : "2X",
         "paymentNumberChosed" : 2,
         "totalToRefund" : 7,
         "amountFees" : 1,
         "paymentAmount" : 6,
         "item" : [ {
           "id" : null,
           "amount" : 4,
           "paymentIndex" : 1,
           "doDateToPay" : "2025-02-02T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Premier paiement incluant les frais"
         }, {
           "id" : null,
           "amount" : 3,
           "paymentIndex" : 2,
           "doDateToPay" : "2025-03-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Dernier paiement"
         } ]
       }, {
         "name" : "3X",
         "paymentNumberChosed" : 3,
         "totalToRefund" : 7,
         "amountFees" : 1,
         "paymentAmount" : 6,
         "item" : [ {
           "id" : null,
           "amount" : 3,
           "paymentIndex" : 1,
           "doDateToPay" : "2025-02-02T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Premier paiement incluant les frais"
         }, {
           "id" : null,
           "amount" : 2,
           "paymentIndex" : 2,
           "doDateToPay" : "2025-03-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Paiement 2"
         }, {
           "id" : null,
           "amount" : 2,
           "paymentIndex" : 3,
           "doDateToPay" : "2025-04-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Dernier paiement"
         } ]
       }, {
         "name" : "4X",
         "paymentNumberChosed" : 4,
         "totalToRefund" : 7,
         "amountFees" : 1,
         "paymentAmount" : 6,
         "item" : [ {
           "id" : null,
           "amount" : 3,
           "paymentIndex" : 1,
           "doDateToPay" : "2025-02-02T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Premier paiement incluant les frais"
         }, {
           "id" : null,
           "amount" : 2,
           "paymentIndex" : 2,
           "doDateToPay" : "2025-03-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Paiement 2"
         }, {
           "id" : null,
           "amount" : 2,
           "paymentIndex" : 3,
           "doDateToPay" : "2025-04-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Paiement 3"
         }, {
           "id" : null,
           "amount" : 2,
           "paymentIndex" : 4,
           "doDateToPay" : "2025-05-05T00:00:00Z",
           "realDatePay" : null,
           "paymentCustomerStatus" : "NOT_PAID",
           "payedInLate" : false,
           "comment" : "Dernier paiement"
         } ]
       } ],
       "infosCommand" : "commandName"
     }
   } 
   ```  

---

### **Étape 3 : Gestion des Callbacks**  
**Objectif** : Recevoir un callback lorsque votre client valide son premier paiement.

1. **En-têtes Requis**  :  
   ```http  
   {
     "accept": "application/json",
     "content-type": "application/json",
     "x-api-key": "prime_api_key_ec09544f-5fc1-416f-89d6-7ed7bfbf5e4d",
     "x-hmac-signature": "kCvGd2x3h04q4HPG7FQ1ObGK/c1s6TmoxZC8rRi5gWLJaLuwNajg1mXuOPdXicggWL46BNTz6fK7iE7srcej+Q==" //"signature_hmac:AES HASH MAC using statusPayment+primeClientMisdn+amount using raw application hmacKey"
     ...
   }
   ```  
   
   
   **Corps de la Requête**
   
   ```json  
   {  
     "callbackUrl": "https://votre-site.com/callback",  
     "amount": 5000,  
     "primeClientPhone": "+221767760904",  
     "externalRefId": "CMD_20230920_123",  
     "statusPayment": "SUCCESS",  
     "datePayment": "2023-09-20T14:30:00Z",  
     "primeInternalPaymentRef": "PAY_550e8400"  
   }  
   
   ```  

2. **Validation de la Signature HMAC** :  
   - Concaténez : `statusPayment + primeClientPhone + amount`.  
    
   - Générez le HMAC avec la `hmacKey` (déchiffrée) :  
     
   - Comparez avec `X-HMAC-SIGNATURE` pour authenticité.  

3. **Actions** :  
   - Mettez à jour le statut de la commande dans votre système.  
   - Envoyez une confirmation au client (si réussi).  

---

### **Étape 5 : Surveillance**  
3. **Support** :  
   - Contactez `support@prime.store` en cas de problème persistant.  

---

## **Bonnes Pratiques**  
- **Test en Sandbox** : Utilisez l’environnement de test avant le déploiement en production.  
- **Validation des Entrées** : Vérifiez toujours le format des numéros de téléphone et des URLs.  

---

## **Ressources Utiles**  
- **Utilisation** : Voir l'utilisation des SDK Java, Node.js, PHP.  
- **Support 24/7** : soon.  

**💡 Conseil** : Commencez avec des petits montants pour valider l’intégration avant de traiter des transactions critiques.
