package net.guides.springboot2.springboot2jpacrudexample.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.springboot2jpacrudexample.exception.Message;
import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Formation;
import net.guides.springboot2.springboot2jpacrudexample.repository.FormationRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.FormationRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class FormationController {
    @Autowired
    FormationRepository repository;


    @Autowired  ServletContext context;
    @GetMapping("/articles")
    public List<Formation> ConsulterListArticle() {
        System.out.println("Afficher les articles");

        List<Formation> articles = new ArrayList<>();
        repository.findAll().forEach(articles::add);

        return articles;
    }









    @GetMapping("/getAll")
    public ResponseEntity<List<String>> getAll ()
    {
        List<String> listArt = new ArrayList<String>();
        String filesPath = context.getRealPath("/Images");
        File filefolder = new File(filesPath);
        if (filefolder != null) {
            for (File file : filefolder.listFiles()) {
                if (!file.isDirectory()) {
                    String encodeBase64 = null;
                    try {
                        String extension = FilenameUtils.getExtension(file.getName());
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[(int) file.length()];
                        fileInputStream.read(bytes);
                        encodeBase64 = Base64.getEncoder().encodeToString(bytes);
                        listArt.add("data:image/" + extension + ";base64," + encodeBase64);
                        fileInputStream.close();


                    } catch (Exception e) {

                    }
                }
            }
        }
        return new ResponseEntity<List<String>>(listArt, HttpStatus.OK);
    }








    @PostMapping("/articles")
    public ResponseEntity<Message> AjouterArticle (@RequestParam("file") MultipartFile file,
                                                   @RequestParam("article") String article) throws JsonParseException , JsonMappingException , Exception {


        System.out.println("Article ajouté");
        Formation arti = new ObjectMapper().readValue(article, Formation.class);
        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit) {
            new File(context.getRealPath("/Images/")).mkdir();

        }
        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename);
        File serverFile = new File(context.getRealPath("/Images/" + File.separator + newFileName));
        try {
            System.out.println("Image");
            FileUtils.writeByteArrayToFile(serverFile, file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }


        arti.setFileName(newFileName);




        if (arti.getNom().length()==0 || arti.getFileName().length()==0 ||arti.getDescription().length()==0
        )
        {
            return ResponseEntity
                    .badRequest()
                    .body(new Message("Ce champ est obligatoire"));
        }






        if(repository.existsByNom(arti.getNom()))
            return new ResponseEntity(new Message("Le nom  existe déja "), HttpStatus.BAD_REQUEST);


        Formation art = repository.save(arti);

        return new ResponseEntity(new Message("Produit ajouté avec succès"), HttpStatus.OK);

    }


    @GetMapping("/articles/{id}")
    public ResponseEntity<Formation> getArticleById(@PathVariable(value = "id") Long Id)
            throws ResourceNotFoundException {
        String filesPath = context.getRealPath("/Images");
        File filefolder = new File(filesPath);
        List<String> listArt = new ArrayList<String>();
        if (filefolder != null)
        {
            for (File file :filefolder.listFiles())
            {
                if(!file.isDirectory())
                {
                    String encodeBase64 = null;
                    try {
                        String extension = FilenameUtils.getExtension(file.getName());
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[(int)file.length()];
                        fileInputStream.read(bytes);
                        encodeBase64 = Base64.getEncoder().encodeToString(bytes);
                        listArt.add("data:image/"+extension+";base64,"+encodeBase64);
                        fileInputStream.close();


                    }catch (Exception e){

                    }
                }
            }
        }
        Formation Article = repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException(" :: " + Id));
        return ResponseEntity.ok().body(Article);


    }

    @GetMapping("/Imgarticles/{id}")
    public byte[] getPhoto(@PathVariable("id") Long id) throws Exception{
        Formation Article   = repository.findById(id).get();
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Article.getFileName()));
    }




    @DeleteMapping("/articles/{id}")
    public Map<String, Boolean> supprimerArticle(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Formation Article = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article n'exite pas:: " + id));
        repository.delete(Article);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }



    @PutMapping("/articles/{id}")
    public ResponseEntity<Formation> ModifierArticle (@PathVariable("id") long id, @RequestBody Formation Article,@RequestParam("file") MultipartFile file) {
        System.out.println("Modification d'un article avec l'id = " + id + "..");
        Optional<Formation> ArticleInfo = repository.findById(id);
        if (ArticleInfo.isPresent()) {


            boolean isExit = new File(context.getRealPath("/Images/")).exists();
            if (!isExit) {
                new File(context.getRealPath("/Images/")).mkdir();

            }
            String filename = file.getOriginalFilename();
            String newFileName = FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename);
            File serverFile = new File(context.getRealPath("/Images/" + File.separator + newFileName));
            try {
                System.out.println("Image");
                FileUtils.writeByteArrayToFile(serverFile, file.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Formation article = ArticleInfo.get();
            article.setNom(Article.getNom());
            article.setDate(Article.getDate());

            article.setDescription(Article.getDescription());
            article.setFileName(Article.getFileName());




            return new ResponseEntity<>(repository.save(Article), HttpStatus.OK);
        } else {
            return new ResponseEntity(new Message("Produit modifié avec succès"), HttpStatus.OK);
        }
    }

}
