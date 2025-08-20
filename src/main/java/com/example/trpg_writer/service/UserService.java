package com.example.trpg_writer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.trpg_writer.entity.User;
import com.example.trpg_writer.form.SignupForm;
import com.example.trpg_writer.form.UserEditForm;
import com.example.trpg_writer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.storage-dir}")
    private String storageDir;

    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();

        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public void update(UserEditForm userEditForm, MultipartFile imageFile, User user) {
        user.setName(userEditForm.getName());
        user.setIntroduction(userEditForm.getIntroduction());

        // 画像ファイルが選択されている場合のみ、保存処理を行う
        if (imageFile != null && !imageFile.isEmpty()) {
            // 古い画像があれば削除する（任意）
            if (user.getImageName() != null && !user.getImageName().isEmpty()) {
                deleteImageFile(user.getImageName());
            }

            String imageName = imageFile.getOriginalFilename();
            String hashedImageName = generateNewImageName(imageName);
            Path filePath = Paths.get(storageDir, "user_images", hashedImageName);
            copyImageFile(imageFile, filePath);
            user.setImageName(hashedImageName);
        }

        userRepository.save(user);
    }

    private String generateNewImageName(String originalImageName) {
        String extension = getFileExtension(originalImageName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void copyImageFile(MultipartFile imageFile, Path filePath) {
        try {
            // 保存先ディレクトリが存在しない場合は作成する
            Files.createDirectories(filePath.getParent());
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // ここで適切な例外処理を行うことが望ましい
            throw new RuntimeException("画像の保存に失敗しました。", e);
        }
    }

    private void deleteImageFile(String imageName) {
        try {
            Path filePath = Paths.get(storageDir, "user_images", imageName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // 削除失敗時のエラーハンドリング
        }
    }

    @Transactional
    public void delete(User user) {
        user.setDeleted(true);
        userRepository.save(user);
    }
}
