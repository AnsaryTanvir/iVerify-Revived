<?php

    class EncryptionHandler{

        private $publicKey;
        private $privateKey;

        public function loadKeys($publicKeyPath, $privateKeyPath){
            $this->publicKey  = openssl_pkey_get_public( 'file://' . $publicKeyPath );
            $this->privateKey = openssl_pkey_get_private('file://' . $privateKeyPath);

            if (!$this->publicKey || !$this->privateKey) {
                throw new RuntimeException("Error loading keys: " . openssl_error_string());
            }
        }

        public function exportPublicKey(){
            $publicKeyDetails = openssl_pkey_get_details($this->publicKey);
            return $publicKeyDetails['key'];
        }

        public function exportPrivateKey(){
            if (!openssl_pkey_export($this->privateKey, $privateKeyText)) {
                throw new RuntimeException("Error exporting private key: " . openssl_error_string());
            }

            return $privateKeyText;
        }

        public function encryptData($data){
            $encryptedData = '';
            if (!openssl_public_encrypt($data, $encryptedData, $this->publicKey, OPENSSL_PKCS1_OAEP_PADDING)) {
                throw new RuntimeException("Error encrypting data: " . openssl_error_string());
            }

            return $encryptedData;
        }

        public function decryptData($encryptedData){
            $decryptedData = '';
            if (!openssl_private_decrypt($encryptedData, $decryptedData, $this->privateKey, OPENSSL_PKCS1_OAEP_PADDING)) {
                throw new RuntimeException("Error decrypting data: " . openssl_error_string());
            }

            return $decryptedData;
        }

        public function displayKeys(){

            echo "Public Key:<br>";
            echo $this->exportPublicKey() . "<br><br>";

            echo "Private Key:<br>";
            echo $this->exportPrivateKey() . "<br><br>";
        }

        // From PHP 8 it has been deprecated
        public function closeKeys(){
            // openssl_free_key($this->publicKey);
            // openssl_free_key($this->privateKey);

        }

        public function __destruct(){
            $this->closeKeys();
        }
    }


    // try {

    //     $publicKeyPath  = 'public_key.pem';
    //     $privateKeyPath = 'private_key.pem';
        
    //     $encryptionHandler = new EncryptionHandler();
    //     $encryptionHandler->loadKeys($publicKeyPath, $privateKeyPath);

    //     // Display keys
    //     $encryptionHandler->displayKeys();

    //     // Encrypt data
    //     $dataToEncrypt = "Hello World! This is a great way to know about the product so to say it is the best";
    //     $encryptedData = $encryptionHandler->encryptData($dataToEncrypt);
    //     echo "Encrypted Data:<br>";
    //     echo bin2hex($encryptedData) . "<br>";

    //     // Decrypt data
    //     $decryptedData = $encryptionHandler->decryptData($encryptedData);
    //     echo "Decrypted Data:<br>";
    //     echo $decryptedData . "<br>";

    // } catch (RuntimeException $e) {
    //     echo "Error: " . $e->getMessage() . "<br>";
    // }

?>