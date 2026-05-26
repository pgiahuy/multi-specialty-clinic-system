import { useContext, useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Spinner, Badge } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import { authApis, endpoint } from "../../configs/Apis";

const normalizeStatus = (status) => {
  if (!status) return "unknown";
  const s = status.toLowerCase();
  if (s.includes("pending") || s.includes("đang chờ")) return "pending";
  if (s.includes("completed") || s.includes("đã hoàn thành")) return "completed";
  if (s.includes("cancel") || s.includes("đã hủy")) return "cancelled";
  return "other";
};

const Home = () => {
    return (
        <div>My Home</div>
    );
};

export default Home;
