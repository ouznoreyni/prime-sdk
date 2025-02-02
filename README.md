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
4. **Réponse Réussie** :  
   ```json  
   {  
     "paymentId": "paymentId",  
     "deepLink": "deepLink",  
     "status": "PENDING"  
   }  
   ```  

---

### **Étape 3 : Gestion des Callbacks**  
**Objectif** : Recevoir un callback lorsque votre client valide son premier paiement.

1. **Format du Callback** :  
   ```http  
   POST https://votre-site.com/callback  
   X-API-KEY: "prime_api_key_550e8400... API KEY"  
   X-HMAC-SIGNATURE: "signature_hmac:AES HASH MAC using statusPayment+primeClientMisdn+amount using raw application hmacKey"  
   ```  
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
     ```javascript  
     const data = "SUCCESS+221761234567+5000";  
     ```  
   - Générez le HMAC avec la `hmacKey` (déchiffrée) :  
     ```javascript  
     const hmac = crypto.createHmac('sha256', hmacKey).update(data).digest('hex');  
     ```  
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