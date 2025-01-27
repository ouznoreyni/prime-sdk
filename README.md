**Documentation de l'API Marchand Distant**  

L’**API Marchand Distant** permet aux partenaires commerciaux d’intégrer un système de paiement sécurisé directement dans leurs applications ou sites web. Conçue pour les transactions programmatiques, cette API offre une flexibilité totale tout en garantissant la sécurité des données et la traçabilité des opérations.  

### **Avantages Clés**  
- **Intégration Simplifiée** : Des endpoints RESTful bien documentés.  
- **Sécurité Renforcée** : Authentification par clé API, chiffrement AES256, et signatures HMAC.  
- **Notifications en Temps Réel** : Callbacks automatiques pour informer du statut des paiements.  
- **Compatibilité** : Supporte les applications web, mobiles, ou systèmes backend.  

### **Prérequis**  
1. Compte marchand validé sur la plateforme.  
2. Accès administrateur pour générer les clés API.  
3. Serveur HTTPS pour recevoir les callbacks.  

---

## **Étapes d’Intégration**  

### **Étape 1 : Génération des Clés API**  
**Objectif** : Obtenir les identifiants pour authentifier les requêtes.  

1. **Via l'interface** :  
   soon
2. **Email** :  
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
5. **Actions** :  
   - Envoyez le `deepLink` au client (SMS, email, ou notification in-app).  

---

### **Étape 3 : Gestion des Callbacks**  
**Objectif** : Recevoir et valider les notifications de statut de paiement.  

1. **Format du Callback** :  
   ```http  
   POST https://votre-site.com/callback  
   X-API-KEY: "prime_api_key_550e8400..."  
   X-HMAC-SIGNATURE: "signature_hmac"  
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

### **Étape 4 : Gestion des Erreurs et Re-tentatives**  
1. **Scénarios d’Échec** :  
   - Réseau instable, timeout, ou réponse HTTP ≠ 200.  
2. **Politique de Re-tentative** :  
   - 3 tentatives automatiques avec délai exponentiel (ex: 1 min, 5 min, 15 min).  
3. **Journalisation** :  
   - Logguez toutes les tentatives et erreurs pour audit.  

---

### **Étape 5 : Surveillance et Reporting**  
1. **Dashboard** :  
   - Consultez les transactions en temps réel via le portail marchand.  
2. **Alertes** :  
   - Configurez des notifications pour les échecs de callback ou paiements refusés.  
3. **Support** :  
   - Contactez `support@prime.store` en cas de problème persistant.  

---

## **Bonnes Pratiques**  
- **Test en Sandbox** : Utilisez l’environnement de test avant le déploiement en production.  
- **Cycle de Vie des Clés** : Régénérez les clés API tous les 3 mois.  
- **Validation des Entrées** : Vérifiez toujours le format des numéros de téléphone et des URLs.  

---

## **Ressources Utiles**  
- **Documentation Technique** : [https://docs.prime.store](https://docs.prime.store)  
- **Bibliothèques SDK** : Voir l'utilisation des SDK Java, Node.js, PHP.  
- **Support 24/7** : soon.  

**💡 Conseil** : Commencez avec des petits montants pour valider l’intégration avant de traiter des transactions critiques.